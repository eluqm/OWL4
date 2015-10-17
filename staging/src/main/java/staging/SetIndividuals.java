package staging;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.coode.owlapi.owlxmlparser.OWLXMLParser;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplBoolean;

//import com.sun.java.util.jar.pack.Package.Class.Field;

public class SetIndividuals {
	
	HashMap<Class, OWLDatatype> typess = new HashMap<Class,OWLDatatype>();
	SetIndividuals(OWLOntologyManager m)
	{
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLDatatype booleans = factory
				.getBooleanOWLDatatype();
		OWLDatatype flo= factory.getFloatOWLDatatype();
		OWLDatatype literalDatatype = factory
                .getOWLDatatype(IRI.create("http://www.w3.org/2001/XMLSchema#string"));
		OWLDatatype integers = factory
				.getIntegerOWLDatatype();
		OWLDatatype datess = factory
				.getOWLDatatype(IRI.create("http://www.w3.org/2001/XMLSchema#dateTime"));
		//OWLDa;
		typess.put(boolean.class, booleans);
		typess.put(Float.class, flo);
		typess.put(String.class, literalDatatype);
		typess.put(Integer.class,integers);
		typess.put(Boolean.class, factory.getBooleanOWLDatatype());
		typess.put(java.util.Date.class, datess);
		typess.put(java.util.ArrayList.class, literalDatatype);
		
	}
	
	void personIndividuals(OWLOntologyManager m,OWLOntology o,List<AnnotationsAIM4> ao,File fileformated) throws OWLOntologyStorageException
	{
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls = m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#Person"));

		// iterate over all patient annotation 
		for (AnnotationsAIM4 element : ao) {
			
		    
		    OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+element.getPerson().getName().toString()));
			OWLAxiom axiom = factory.getOWLClassAssertionAxiom(cls,ind);
			AddAxiom addAxion = new AddAxiom(o, axiom);
			m.applyChange(addAxion);
		
		}
		
		
		
		
		OWLOntologyFormat format = m.getOntologyFormat(o);
		
		m.saveOntology(o,format,IRI.create(fileformated.toURI()));
		
		
		
	}
	// create individuals of annotation class
	void annotationIndividuals(OWLOntologyManager m,OWLOntology o,List<AnnotationsAIM4> ao,File fileformated) throws OWLOntologyStorageException
	{
		String IRIontology = o.getOntologyID().getOntologyIRI().toString();
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls = m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#ImageAnnotation"));
		
		//	object properties from annotationEntity hasObservationEntity
		OWLObjectProperty hasPhysical= factory.getOWLObjectProperty(IRI.create(IRIontology + "#hasPhysicalEntity"));
		// object properties from annotationEntity hasPhysicalENtity
		OWLObjectProperty hasmObservation  = factory.getOWLObjectProperty(IRI.create(IRIontology + "#hasImagingObservation"));
		// object properties from annotationEntity hasmarkup
		OWLObjectProperty hasmarkup = factory.getOWLObjectProperty(IRI.create(IRIontology + "#hasMarkupEntity"));
		// object properties from annotationEntity hasCalculationEntity
				OWLObjectProperty hascalculation = factory.getOWLObjectProperty(IRI.create(IRIontology + "#hasCalculationEntity"));
		
		List<MarkupEntity> markelements;
		List<ImagingObservationEntity> obs;
		List<ImagingPhysicalEntity> phys;
		List<CalculationEntity> calc;
		
		OWLIndividual indannotation;
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		
		// iterate over all patient annotation 
		for (AnnotationsAIM4 element : ao) {
			List<Annotation> elements= (List<Annotation>) element.getImageAnnotations();
			for(Annotation ann: elements)
			{
				
				
				OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+ann.getUniqueIdentifier().toString()));
				OWLAxiom axiom = factory.getOWLClassAssertionAxiom(cls,ind);
				axioms.add(axiom);
				
				
				/**
				 * adding Dataproperties 
				 * 
				 * */
				try {
					setDataProperties(ann, cls, axioms, factory, o, ind);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IntrospectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						        
				/**
				 * adding OBjectproperties , 
				 * 
				 * */		        
		        // set objectproperty hasmarkupentity domain: class imageAnnotation range: markupEntity 1 -> 1...*
			    // get list of markupentity from markupentitycollection
			    markelements = (List<MarkupEntity>) ann.getMarkupEntityCollection();
			    obs = (List<ImagingObservationEntity>)ann.getImagingObservationEntityCollection();
			    phys = (List<ImagingPhysicalEntity>)ann.getImagingPhysicalEntityCollection();
			    calc= (List<CalculationEntity>)ann.getCalculationEntityCollection();
			    //iterate over all markupentities from an annotation
			    
			    for(MarkupEntity an: markelements) {
			    	indannotation=factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+an.getUniqueIdentifier().toString()));
			    	
			    	axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasmarkup,ind,indannotation));
			    }
			    for(ImagingObservationEntity an: obs) {
			    	indannotation=factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+an.getUniqueIdentifier().toString()));
			    	
			    	axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasmObservation,ind,indannotation));
			    }
			    for(ImagingPhysicalEntity an: phys) {
			    	indannotation=factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+an.getUniqueIdentifier().toString()));
			    	
			    	axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasPhysical,ind,indannotation));
			    }
			    for(CalculationEntity an: calc) {
			    	indannotation=factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+an.getUniqueIdentifier().toString()));
			    	
			    	axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hascalculation,ind,indannotation));
			    }
		        
		        
		        
		        
		        
		        m.addAxioms(o, axioms);
				
				
				
				
			}
		    
		    axioms.clear();
			
		}
		
				OWLOntologyFormat format = m.getOntologyFormat(o);
				
				m.saveOntology(o,format,IRI.create(fileformated.toURI()));
		
	}
	void imageannotationscollectIndividuals(OWLOntologyManager m,OWLOntology o,List<AnnotationsAIM4> ao,File fileformated) throws OWLOntologyStorageException
	{
		String IRIontology = o.getOntologyID().getOntologyIRI().toString();
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls = m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#ImageAnnotationCollection"));
		
		// best yet ?...  list<objectProperty> ??
	    //Objectproperty from class
		OWLObjectProperty hasImageAnnotations = factory.getOWLObjectProperty(IRI
                .create(IRIontology + "#hasImageAnnotations"));
		OWLObjectProperty hasPerson = factory.getOWLObjectProperty(IRI
                .create(IRIontology + "#hasPerson"));
		
		
		List<Annotation> elements;
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		
		OWLIndividual perannotation;
		for (AnnotationsAIM4 element : ao) {
			
			//create imageannotationCollection individual
		    OWLIndividual ind = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+element.getUid()));
		    //set axioms 
		    axioms.add(factory.getOWLClassAssertionAxiom(cls,ind));
		    OWLIndividual indannotation;
		    
		    
		    /**
			 * adding OBjectproperties , 
			 * 
			 * */
		    // set objectproperty hasPerson domain: class imageAnnotationCollection range: Person 1-> 0...1
		    perannotation=factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+element.getPerson().getName().toString()));
		    axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasPerson,ind,perannotation));
		    
		    // set objectproperty hasImageAnnotation domain: class imageAnnotationCollection range: ImageAnnotation 1 -> 1...*
		    // get list of imageannotation from imageannotationcollection
		    elements = (List<Annotation>) element.getImageAnnotations();
		    //iterate over all annotations
		    for(Annotation an: elements) {
		    	indannotation=factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+an.getUniqueIdentifier().toString()));
		    	
		    	axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasImageAnnotations,ind,indannotation));
		    }
		    
		    
		   
		
		}
		
		 
			
			m.addAxioms(o, axioms);
			axioms.clear();
		
		
	}
	void markupIndividuals(OWLOntologyManager m,OWLOntology o,List<AnnotationsAIM4> ao,File fileformated) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IntrospectionException
	{	String IRIontology = o.getOntologyID().getOntologyIRI().toString();
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls ;
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		
		
		OWLObjectProperty hasSpatialCoordinate = factory.getOWLObjectProperty(IRI
                .create(IRIontology + "#hasTwoDimensionSpatialCoordinate"));
		OWLIndividual SpatialCoordinateind;
		// iterate over all patient annotation 
		for (AnnotationsAIM4 element : ao) {
			List<Annotation> elements= (List<Annotation>) element.getImageAnnotations();
			for(Annotation ann: elements)
			{
				
				List<MarkupEntity> markelements= (List<MarkupEntity>) ann.getMarkupEntityCollection();
				
				
				for(MarkupEntity markindv:markelements)
				{
					
					cls=m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#"+markindv.getType()));
					
					PrefixManager pm = new DefaultPrefixManager(o.getOntologyID().getOntologyIRI().toString()+"#");
					
					OWLDataProperty hasproperty;
					OWLLiteral literal; 
					
					
					
					
					
					OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+markindv.getUniqueIdentifier()));
					axioms.add(factory.getOWLClassAssertionAxiom(cls,ind));
					
					//Boolean tem=new Boolean("true");
					
					BeanInfo beanInfo = Introspector.getBeanInfo(markindv.getClass());
					
					// set all dataProperties of markupentity individuals 
					for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
					    // get the name of class property
						String propertyName = propertyDesc.getName();
					    try {
					    	//get value of property
							Object value = propertyDesc.getReadMethod().invoke(markindv);
							if(value != null ){
								if(!propertyName.equals("class") && !propertyName.equals("metaClass") && !propertyName.endsWith("Collection"))
								{
									//creating ... dataProperty into ontology
									hasproperty = factory.getOWLDataProperty(propertyName, pm);
									//literal=(value.getClass() == tem.getClass())?factory.getOWLLiteral(value.toString(),booleans):factory.getOWLLiteral((String)value,literalDatatype);;
									
									literal=factory.getOWLLiteral(value.toString(),typess.get(value.getClass()));
									axioms.add(factory.getOWLDataPropertyAssertionAxiom(hasproperty,ind,literal));
								
								}
								if(propertyName.endsWith("Collection"))
								{	
									
									spatialcoordinatecollectIndividuals(m, o, markindv,markindv.getType());
								}
							
								}
					    } catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			       
					
					/**
					 * adding OBjectproperties , 
					 * 
					 * */
					
									    
				    if(markindv.getType().equals("TwoDimensionMultiPoint"))
					{	
						twoDimensionType tempp= (twoDimensionType) markindv; 
						for(twoDSCCollection collectt :(List<twoDSCCollection>)tempp.getTwoDspatialCoordinateCollection())
						{
							SpatialCoordinateind=factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+markindv.getUniqueIdentifier()+collectt.getCoordinateIndex()));
					    	
					    	axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasSpatialCoordinate,ind,SpatialCoordinateind));
											
						}
					}
				    
				    				
					
				}
				
				
			}
		    
		    
			
		}
				
		m.addAxioms(o, axioms);
		axioms.clear();
	}
	
	//create individuals from markupentity
	@SuppressWarnings("unchecked")
	void spatialcoordinatecollectIndividuals(OWLOntologyManager m,OWLOntology o,MarkupEntity ao,String tip) throws IntrospectionException, IllegalAccessException
	{
		
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls;
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
				
		if(tip.equals("TwoDimensionMultiPoint"))
		{	
			twoDimensionType tempp= (twoDimensionType) ao; 
			
			
			for(twoDSCCollection collectt :(List<twoDSCCollection>)tempp.getTwoDspatialCoordinateCollection())
			{
				cls=m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#TwoDimensionSpatialCoordinate"));
				
				
				OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+ao.getUniqueIdentifier()+collectt.getCoordinateIndex()));
								
				//pds: the individual is adding in the follow method
				setDataProperties(collectt, cls, axioms, factory, o, ind);
								
			}
		}
		m.addAxioms(o, axioms);
		axioms.clear();
	}
	

	void setDataProperties(Object markindv,OWLClass cls,Set<OWLAxiom> axioms,OWLDataFactory factory,OWLOntology o,OWLNamedIndividual ind) throws IntrospectionException, IllegalAccessException
	{
		//System.out.println("*****************************+++");
		PrefixManager pm = new DefaultPrefixManager(o.getOntologyID().getOntologyIRI().toString()+"#");
		
		
		OWLDataProperty hasproperty;
		OWLLiteral literal; 
		
		axioms.add(factory.getOWLClassAssertionAxiom(cls,ind));
		
		
		
		BeanInfo beanInfo = Introspector.getBeanInfo(markindv.getClass());
		//System.out.printf("typo de datos %s",flo.getClass().toString());
		// set all dataProperties of markupentity individuals 
		for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
		    // get the name of class property
			String propertyName = propertyDesc.getName();
		    try {
		    	//get value of property
				Object value = propertyDesc.getReadMethod().invoke(markindv);
				
				if(value != null ){
					
					//System.out.println(propertyName+" :"+value + " :" + typess.get(value.getClass()));
					if(propertyName.equals("typeCode"))
					{	hasproperty = factory.getOWLDataProperty(propertyName, pm);						
						for(HashMap<String,String>values:(ArrayList<HashMap<String, String>>) value)
						{
							//System.out.println(values.toString());
							literal=factory.getOWLLiteral(values.toString(),typess.get(value.getClass()));
							axioms.add(factory.getOWLDataPropertyAssertionAxiom(hasproperty,ind,literal));
						}
						
					}
					if(!propertyName.equals("class") && !propertyName.equals("metaClass") && !propertyName.endsWith("Collection") && !propertyName.equals("typeCode"))
					{
						//creating ... dataProperty ontology
						hasproperty = factory.getOWLDataProperty(propertyName, pm);
						//System.out.println(" "+ value.getClass());
						
						if(value.getClass()== java.util.Date.class){
							SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
							
							literal=factory.getOWLLiteral(formats.format(value),typess.get(value.getClass()));
						}
						else{
							literal=factory.getOWLLiteral(value.toString(),typess.get(value.getClass()));
						}
						
						axioms.add(factory.getOWLDataPropertyAssertionAxiom(hasproperty,ind,literal));
					
					}
					
				
					}
		    } catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	void imagingphysicalentityIndividuals(OWLOntologyManager m,OWLOntology o,List<AnnotationsAIM4> ao,File fileformated) throws OWLOntologyStorageException
	{
		String IRIontology = o.getOntologyID().getOntologyIRI().toString();
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls ;
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		// iterate over all patient annotation 
				for (AnnotationsAIM4 element : ao) {
					List<Annotation> elements= (List<Annotation>) element.getImageAnnotations();
					for(Annotation ann: elements)
					{
						
						//List<ImagingPhysicalEntity> markelements= 
						for(ImagingPhysicalEntity collectt :(List<ImagingPhysicalEntity>) ann.getImagingPhysicalEntityCollection())
						{
							//System.out.println(collectt);
							cls=m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#ImagingPhysicalEntity"));
							
							OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+collectt.getUniqueIdentifier()));
											
							//pds: the individual is adding in the follow method
							try {
							//	System.out.println("entre aca");
								setDataProperties(collectt, cls, axioms, factory, o, ind);
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IntrospectionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
											
						}
					
					
					}
				}
				
				m.addAxioms(o, axioms);
				axioms.clear();
				//OWLOntologyFormat format = m.getOntologyFormat(o);
				//m.saveOntology(o,format,IRI.create(fileformated.toURI()));
		
	}
	
	
	void imagingObservationentityIndividuals(OWLOntologyManager m,OWLOntology o,List<AnnotationsAIM4> ao,File fileformated)
	{
		//String IRIontology = o.getOntologyID().getOntologyIRI().toString();
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls ;
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		// iterate over all patient annotation 
				for (AnnotationsAIM4 element : ao) {
					List<Annotation> elements= (List<Annotation>) element.getImageAnnotations();
					for(Annotation ann: elements)
					{
						
						//List<ImagingPhysicalEntity> markelements= 
						for(ImagingObservationEntity collectt :(List<ImagingObservationEntity>) ann.getImagingObservationEntityCollection())
						{
							//System.out.println(collectt);
							cls=m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#ImagingObservationEntity"));
							
							OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+collectt.getUniqueIdentifier()));
											
							//pds: the individual is adding in the follow method
							try {
							//	System.out.println("entre aca");
								setDataProperties(collectt, cls, axioms, factory, o, ind);
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IntrospectionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
											
						}
					
					
					}
				}
				
				m.addAxioms(o, axioms);
				axioms.clear();
		
	}
	
	
	void calculationEntityCollection(OWLOntologyManager m,OWLOntology o,List<AnnotationsAIM4> ao,File fileformated) throws IntrospectionException, IllegalAccessException
	{
		String IRIontology = o.getOntologyID().getOntologyIRI().toString();
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls ;
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		
		
		OWLObjectProperty hasCalculationResult = factory.getOWLObjectProperty(IRI
                .create(IRIontology + "#hasCalculationResult"));
		OWLIndividual calculationresultind;
		// iterate over all patient annotation 
		for (AnnotationsAIM4 element : ao) {
			List<Annotation> elements= (List<Annotation>) element.getImageAnnotations();
			for(Annotation ann: elements)
			{
				List<CalculationEntity> markelements=(List<CalculationEntity>) ann.getCalculationEntityCollection();
				
				for(CalculationEntity calcindv: markelements)
				{
					
					cls=m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#CalculationEntity"));
					
					PrefixManager pm = new DefaultPrefixManager(o.getOntologyID().getOntologyIRI().toString()+"#");
					
					OWLDataProperty hasproperty;
					OWLLiteral literal; 
					
					
					
					
					
					OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+calcindv.getUniqueIdentifier()));
					axioms.add(factory.getOWLClassAssertionAxiom(cls,ind));
					
					//Boolean tem=new Boolean("true");
					
					BeanInfo beanInfo = Introspector.getBeanInfo(calcindv.getClass());
					
					// set all dataProperties of markupentity individuals 
					for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
					    // get the name of class property
						String propertyName = propertyDesc.getName();
					    try {
					    	//get value of property
							Object value = propertyDesc.getReadMethod().invoke(calcindv);
							if(value != null ){
								if(!propertyName.equals("class") && !propertyName.equals("metaClass") && !propertyName.endsWith("Collection"))
								{
									//creating ... dataProperty into ontology
									hasproperty = factory.getOWLDataProperty(propertyName, pm);
									//literal=(value.getClass() == tem.getClass())?factory.getOWLLiteral(value.toString(),booleans):factory.getOWLLiteral((String)value,literalDatatype);;
									
									literal=factory.getOWLLiteral(value.toString(),typess.get(value.getClass()));
									axioms.add(factory.getOWLDataPropertyAssertionAxiom(hasproperty,ind,literal));
								
								}
								if(propertyName.endsWith("Collection"))
								{	
									calculationResultIndividuals(m,o,calcindv);
									//algorithmIndividuals(m, o, calcindv,calcindv.getType());
								}
							
								}
					    } catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			       
					
					/**
					 * adding OBjectproperties , 
					 * 
					 * */
					
									    
				  /*  if(calcindv.getType().equals("TwoDimensionMultiPoint"))
					{	
						twoDimensionType tempp= (twoDimensionType) calcindv; 
						for(twoDSCCollection collectt :(List<twoDSCCollection>)tempp.getTwoDspatialCoordinateCollection())
						{
							SpatialCoordinateind=factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+calcindv.getUniqueIdentifier()+collectt.getCoordinateIndex()));
					    	
					    	axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasSpatialCoordinate,ind,SpatialCoordinateind));
											
						}
					}
				    
				    	*/			
					
				}
				
				
				
				
				}
			}
		m.addAxioms(o, axioms);
		axioms.clear();
	}
	
	void calculationResultIndividuals(OWLOntologyManager m,OWLOntology o,CalculationEntity ao)
	{
		
		
	}

}
