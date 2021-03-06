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
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import jdk.nashorn.internal.runtime.ListAdapter;

import org.coode.owlapi.owlxmlparser.OWLXMLParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.metrics.AxiomCount;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
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
	
	//mappinng of pacient in order to identify if have 2 or more lesion by ImageStudy and Imageannotation
	HashMap<String,List<String>> tempLesion = new HashMap<String, List<String>>();
	String TNMclassification;
	
	SetIndividuals(OWLOntologyManager m,String TNMClassificationType)
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
		TNMclassification=TNMClassificationType;
		
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
		
		
		Set<OWLIndividual> annots = new   HashSet<OWLIndividual>();
		/***
			all individuals diferents
		 ****/
		annots=cls.getIndividuals(o);
		System.out.println("numero de individuals: "+annots.size());
		OWLDifferentIndividualsAxiom differentCountriesAx =  
				factory.getOWLDifferentIndividualsAxiom(annots);
		m.addAxiom(o, differentCountriesAx);
		/**********************************/
		OWLOntologyFormat format = m.getOntologyFormat(o);

		m.saveOntology(o,format,IRI.create(fileformated.toURI()));
		
		
		
	}
	// create individuals of annotation class
	@SuppressWarnings("unchecked")
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
				OWLObjectProperty hasreferenceImage = factory.getOWLObjectProperty(IRI.create(IRIontology + "#hasImageReference"));
				OWLObjectProperty haslesion = factory.getOWLObjectProperty(IRI.create(IRIontology + "#hasLesion"));
		
		List<MarkupEntity> markelements;
		List<ImagingObservationEntity> obs;
		List<ImagingPhysicalEntity> phys;
		List<CalculationEntity> calc;
		List<ImageReferenceEntity> reff;
		OWLIndividual indannotation;
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		
		// iterate over all patient annotation 
		for (AnnotationsAIM4 element : ao) {
			List<Annotation> elements= (List<Annotation>) element.getImageAnnotations();
			for(Annotation ann: elements)
			{
				OWLNamedIndividual ind = 
						factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+ann.getUniqueIdentifier().toString()));
				OWLAxiom axiom = 
						factory.getOWLClassAssertionAxiom(cls,ind);
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
			    reff=(List<ImageReferenceEntity>)ann.getImagereferencedCollection();
			    //iterate over all markupentities from an annotation
			    
			    for(MarkupEntity an: markelements) {
			    	indannotation=
			    			factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+an.getUniqueIdentifier().toString()));
			    	axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasmarkup,ind,indannotation));
			    }
			    for(ImagingObservationEntity an: obs) {
			    	PrefixManager pm = 
			    			new DefaultPrefixManager(o.getOntologyID().getOntologyIRI().toString()+"#");
		    		OWLDataProperty hasproperty;
		    		OWLLiteral literal; 
		    		hasproperty = 
		    				factory.getOWLDataProperty("hasLesionBool", pm);	
		    		indannotation=
		    				factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+an.getUniqueIdentifier().toString()));
			    	axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasmObservation,ind,indannotation));
			    	
			    	if(an.getLabel().equals("Lesion")){
			    							
						//for(HashMap<String,String>values:(ArrayList<HashMap<String, String>>) value)
							//System.out.println(values.toString());
							literal=factory.getOWLLiteral("true",typess.get(Boolean.class));
							axioms.add(factory.getOWLDataPropertyAssertionAxiom(hasproperty,ind,literal));
							/**
							 * adding OBjectproperties , 
							 * 
							 * */
							indannotation=
									factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+"Lesion"+an.getUniqueIdentifier().toString()));
							axioms.add(factory.getOWLObjectPropertyAssertionAxiom(haslesion,ind,indannotation));
			    		
			    	}
			    				    			    	
			    }
			    if(phys.size()==0){
			    	PrefixManager pm = new DefaultPrefixManager(o.getOntologyID().getOntologyIRI().toString()+"#");
		    		OWLDataProperty hasproperty;
		    		OWLLiteral literal; 
		    		hasproperty = factory.getOWLDataProperty("hasLesionBool", pm);
		    		literal=factory.getOWLLiteral("false",typess.get(Boolean.class));
					axioms.add(factory.getOWLDataPropertyAssertionAxiom(hasproperty,ind,literal));
			    }
			    else{
			    	for(ImagingPhysicalEntity an: phys) {
			    	
			    		indannotation=
			    				factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+an.getUniqueIdentifier().toString()));
			    		axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasPhysical,ind,indannotation));
			    }
			    }
			    
			    for(CalculationEntity an: calc) {
			    		indannotation=
			    				factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+an.getUniqueIdentifier().toString()));
			    	   	axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hascalculation,ind,indannotation));
			    }
			   
			    for(ImageReferenceEntity an: reff) {
			    		indannotation=
			    				factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+an.getUniqueIdentifier().toString()));
			    		axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasreferenceImage,ind,indannotation));
			    }
			   
		       	        
		        m.addAxioms(o, axioms);
							
			}
		    
		    axioms.clear();
			
		}
				
				Set<OWLIndividual> annots = new   HashSet<OWLIndividual>();
				/***
						all individuals diferents
					****/
						annots=cls.getIndividuals(o);
		       		System.out.println("numero de individuals: "+annots.size());
					OWLDifferentIndividualsAxiom differentCountriesAx =  
							factory.getOWLDifferentIndividualsAxiom(annots);
					m.addAxiom(o, differentCountriesAx);
					/**********************************/
		
				OWLOntologyFormat format = m.getOntologyFormat(o);
				
				m.saveOntology(o,format,IRI.create(fileformated.toURI()));
		
	}
	//add new class lesion to locate physical position of tumor on body 
	void addLesionIndividuals(ImagingPhysicalEntity a,ImagingObservationEntity b,Annotation c,Set<OWLAxiom> axions,OWLOntologyManager m,OWLOntology o)
	{	
		String IRIontology = o.getOntologyID().getOntologyIRI().toString();
		OWLDataFactory factory = m.getOWLDataFactory();
		// case : lESION INSIDE THE tnmTYPE organ or outside the organ such as adjacent organs  
		OWLClass cls = m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#Lesion"));
		OWLClass cls2 = m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#outside_Lesion"));
		PrefixManager pm = new DefaultPrefixManager(o.getOntologyID().getOntologyIRI().toString()+"#");
		OWLDataProperty hasproperty;
		OWLLiteral literal; 
		
		
		hasproperty = factory.getOWLDataProperty("isRegionalLymphNodeAffected", pm);
		literal=factory.getOWLLiteral("false",typess.get(Boolean.class));
		
		//creating individuals of lesion realted with organs
		String str=a.getTypeCode().get(1).get("codeSystem");
		String str2organ=a.getTypeCode().get(0).get("codeSystem");
		
		OWLClass cls3 = m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#"+str2organ.replaceAll(" ", "_")));
		
		if(TNMclassification.equals(str2organ)){//case: lesion is inside of organ 
			OWLIndividual ind = 
					factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+"Lesion"+b.getUniqueIdentifier()));
			OWLIndividual indorgan = 
					factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+str2organ+a.getUniqueIdentifier()));
			OWLIndividual ind2 = 
					factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+str.replaceAll(" ", "_")));
			axions.add(factory.getOWLClassAssertionAxiom(cls,ind));
			axions.add(factory.getOWLClassAssertionAxiom(cls3,indorgan));
			/**
			 * adding OBjectproperties , 
			 * 
			 * */
			OWLObjectProperty hasLesion = factory.getOWLObjectProperty(IRI
	                .create(IRIontology + "#isLocatedInSegment"));
			OWLObjectProperty hasLesion2 = factory.getOWLObjectProperty(IRI
	                .create(IRIontology + "#hasLocation"));
			axions.add(factory.getOWLDataPropertyAssertionAxiom(hasproperty,ind,literal));
			axions.add(factory.getOWLObjectPropertyAssertionAxiom(hasLesion,ind,ind2));
			axions.add(factory.getOWLObjectPropertyAssertionAxiom(hasLesion2,ind,indorgan));
			
		}else{
			OWLIndividual indorgan= 
					factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+str2organ.replaceAll(" ", "_")+a.getUniqueIdentifier()));
			OWLIndividual ind = 
					factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+"Lesion"+b.getUniqueIdentifier()));
			axions.add(factory.getOWLClassAssertionAxiom(cls2,ind));
			axions.add(factory.getOWLClassAssertionAxiom(cls3,indorgan));
			/**
			 * adding OBjectproperties , 
			 * 
			 * */
			OWLObjectProperty hasLesion = factory.getOWLObjectProperty(IRI
	                .create(IRIontology + "#hasLocation"));
			
			
			if(!str2organ.replaceAll(" ", "_").equals("lymph_node"))
			
			{	axions.add(factory.getOWLDataPropertyAssertionAxiom(hasproperty,ind,literal));
				axions.add(factory.getOWLObjectPropertyAssertionAxiom(hasLesion,ind,indorgan));
				
			}else{
				
				axions.add(factory.getOWLObjectPropertyAssertionAxiom(hasLesion,ind,indorgan));
				
			}
			}
			
			
		
		
		
		
		
		
		
		
	}
	@SuppressWarnings("unchecked")
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
		    OWLIndividual ind = 
		    		factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+element.getUid()));
		    //set axioms 
		    axioms.add(factory.getOWLClassAssertionAxiom(cls,ind));
		    OWLIndividual indannotation;
		    
		    
		    /**
			 * adding OBjectproperties , 
			 * 
			 * */
		    // set objectproperty hasPerson domain: class imageAnnotationCollection range: Person 1-> 0...1
		    perannotation=
		    		factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+element.getPerson().getName().toString()));
		    axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasPerson,ind,perannotation));
		    
		    // set objectproperty hasImageAnnotation domain: class imageAnnotationCollection range: ImageAnnotation 1 -> 1...*
		    // get list of imageannotation from imageannotationcollection
		    elements = (List<Annotation>) element.getImageAnnotations();
		    //iterate over all annotations
		    for(Annotation an: elements) {
		    	indannotation=
		    		factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+an.getUniqueIdentifier().toString()));
		    axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasImageAnnotations,ind,indannotation));
		    }
		    }
							Set<OWLIndividual> annots = new   HashSet<OWLIndividual>();
				             m.addAxioms(o, axioms);
				             annots=cls.getIndividuals(o);
				      		System.out.println("numero de individuals: "+annots.size());
					             OWLDifferentIndividualsAxiom differentCountriesAx =  
					            		 factory.getOWLDifferentIndividualsAxiom(annots);
					             m.addAxiom(o, differentCountriesAx);
				             
				             
				             axioms.clear();
		
		
	}
	@SuppressWarnings("unchecked")
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
	

	@SuppressWarnings("unchecked")
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
						for(ImagingPhysicalEntity collectt :(List<ImagingPhysicalEntity>) ann.getImagingPhysicalEntityCollection())
						{
							//System.out.println(collectt);
							cls=m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#ImagingPhysicalEntity"));
							
							OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+collectt.getUniqueIdentifier()));
											
							//pds: the individual is adding in the follow method
							try {
							
								setDataProperties(collectt, cls, axioms, factory, o, ind);
								List<ImagingObservationEntity> lisobs=(List<ImagingObservationEntity>)ann.getImagingObservationEntityCollection();
								for(ImagingObservationEntity lisob:lisobs)
								{
									if(lisob.getLabel().equals("Lesion")){addLesionIndividuals(collectt,lisob,ann,axioms,m, o);}
								}
								//falta verificar en el caso de que tengas mas anotaciones y mas observaciones , no hay manera de saber que una observacion pertenece a una determinada lesion
								
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
	
	
	@SuppressWarnings("unchecked")
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
	
	
	@SuppressWarnings("unchecked")
	void calculationEntityCollection(OWLOntologyManager m,OWLOntology o,List<AnnotationsAIM4> ao,File fileformated) throws IntrospectionException, IllegalAccessException
	{
		//String IRIontology = o.getOntologyID().getOntologyIRI().toString();
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls ;
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		
		
		
		//OWLIndividual calculationresultind;
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
									calculationResultIndividuals(m,o,(List<calculationResult>)calcindv.getCalculationResultCollection(),calcindv.getUniqueIdentifier(),calcindv);
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
					
									    
				/*    if(calcindv.getType().equals("TwoDimensionMultiPoint"))
					{	
						twoDimensionType tempp= (twoDimensionType) calcindv; 
						for(twoDSCCollection collectt :(List<twoDSCCollection>)tempp.getTwoDspatialCoordinateCollection())
						{
							SpatialCoordinateind=factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+calcindv.getUniqueIdentifier()+collectt.getCoordinateIndex()));
					    	
					    	axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasSpatialCoordinate,ind,SpatialCoordinateind));
											
						}
					}*/
				    
				    				
					
				}
				
				
				
				
				}
			}
		m.addAxioms(o, axioms);
		axioms.clear();
	}
	
	void calculationResultIndividuals(OWLOntologyManager m,OWLOntology o,List<calculationResult> ao,String uniqueid,CalculationEntity inv) throws IntrospectionException, IllegalAccessException
	{
		String IRIontology = o.getOntologyID().getOntologyIRI().toString();
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls ;
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		
		
		OWLObjectProperty hasCalculationResult = factory.getOWLObjectProperty(IRI
                .create(IRIontology + "#hasCalculationResult"));
		OWLIndividual SpatialCoordinateind;
		
		
		for (calculationResult element : ao) {
			OWLDataProperty hasproperty;
			OWLLiteral literal;
			ExtendedCalculationResult extend;
			//System.out.println(element.getType().toString());
					if(element.getType().equals("ExtendedCalculationResult"))
					{
						extend=(ExtendedCalculationResult) element;
						cls=m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#ExtendedCalculationResult"));
						
						PrefixManager pm = new DefaultPrefixManager(o.getOntologyID().getOntologyIRI().toString()+"#");
						OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+uniqueid+extend.getType()));
						axioms.add(factory.getOWLClassAssertionAxiom(cls,ind));

						//Boolean tem=new Boolean("true");
						
						BeanInfo beanInfo = Introspector.getBeanInfo(extend.getClass());
						
						// set all dataProperties of markupentity individuals 
						for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
						    // get the name of class property
							String propertyName = propertyDesc.getName();
						    try {
						    	//get value of property
								Object value = propertyDesc.getReadMethod().invoke(extend);
								if(value != null ){
									if(!propertyName.equals("class") && !propertyName.equals("metaClass") && !propertyName.endsWith("Collection"))
									{
										//creating ... dataProperty into ontology
										hasproperty = factory.getOWLDataProperty(propertyName, pm);
																				
										literal=factory.getOWLLiteral(value.toString(),typess.get(value.getClass()));
										axioms.add(factory.getOWLDataPropertyAssertionAxiom(hasproperty,ind,literal));
									
									}
									if(propertyName.endsWith("Collection"))
									{	
										calculationData(m,o,extend,uniqueid+extend.getType(),axioms,ind);
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
						
										    
					    	
							//twoDimensionType tempp= (twoDimensionType) calcindv; 
							
								SpatialCoordinateind=factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+inv.getUniqueIdentifier()));
						    	
						    	axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasCalculationResult,SpatialCoordinateind,ind));
												
							
					
					
					
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
					
		m.addAxioms(o, axioms);
		axioms.clear();
	}
	
	@SuppressWarnings("unchecked")
	void calculationData(OWLOntologyManager m,OWLOntology o,ExtendedCalculationResult a,String uniq,Set<OWLAxiom> axioms,OWLNamedIndividual ind)
	{
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLDataProperty hasproperty;
		OWLLiteral literal;
		PrefixManager pm = new DefaultPrefixManager(o.getOntologyID().getOntologyIRI().toString()+"#");
		OWLDatatype flo= factory.getFloatOWLDatatype();
		for(CalculationData ao: (List<CalculationData>)a.getCalculationDataCollection())
		{
			
			hasproperty = factory.getOWLDataProperty("values", pm);
			literal=factory.getOWLLiteral(Float.toString(ao.getValues()),flo);
			axioms.add(factory.getOWLDataPropertyAssertionAxiom(hasproperty,ind,literal));
			
		}
		//setDataProperties(markindv, cls, axioms, factory, o, ind);
		
	}
	void imageSeriesIndividuals(OWLOntologyManager m,OWLOntology o,Set<OWLAxiom> axioms,ImageStudy imaref,OWLNamedIndividual imageStdyind) throws IntrospectionException, IllegalAccessException
	{
		//System.out.println("entreeeeee");
		String IRIontology = o.getOntologyID().getOntologyIRI().toString();
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls ;
		OWLDataProperty hasproperty;
		PrefixManager pm = new DefaultPrefixManager(o.getOntologyID().getOntologyIRI().toString()+"#");
		OWLLiteral literal;
		
		OWLObjectProperty hasImageSerie = factory.getOWLObjectProperty(IRI
                .create(IRIontology + "#hasImageSeries"));
		OWLIndividual imageSrieind;
		
		ImageSeries imgStdy= imaref.getImageSeries();	
		
		cls=m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#ImageSeries"));
		OWLNamedIndividual imageSeries = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#12"+imgStdy.getInstanceUid()));
		axioms.add(factory.getOWLClassAssertionAxiom(cls,imageSeries));
		
		BeanInfo beanInfo = Introspector.getBeanInfo(imgStdy.getClass());
		
		// set all dataProperties of markupentity individuals 
		//System.out.println("/////////////////");
		/*for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
		    // get the name of class property
			String propertyName = propertyDesc.getName();
			
			
		    try {
		    	//get value of property
				Object value = propertyDesc.getReadMethod().invoke(imgStdy);
				
				if(value != null ){
					if(!propertyName.equals("class") && !propertyName.equals("metaClass") && !propertyName.endsWith("Collection"))
					{
						//creating ... dataProperty into ontology
						//System.out.println(propertyName);
						//System.out.println(value);
						hasproperty = factory.getOWLDataProperty(propertyName, pm);
						//literal=(value.getClass() == tem.getClass())?factory.getOWLLiteral(value.toString(),booleans):factory.getOWLLiteral((String)value,literalDatatype);;
						
						literal=factory.getOWLLiteral(value.toString(),typess.get(value.getClass()));
						axioms.add(factory.getOWLDataPropertyAssertionAxiom(hasproperty,imageStdyind,literal));
					
					}
					if(propertyName.endsWith("Collection"))
					{	
						//imageSeriesIndividuals(m,o,axioms,imgStdy,imageStdyind);
						//imagestudyIndividuals(m,o,axioms,imarefindvDicom,ind);
						//spatialcoordinatecollectIndividuals(m, o, imarefindv,imarefindv.getType());
					}
				
					}
		    } catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}/*
		//System.out.println("*******************");
		/**
		 * adding OBjectproperties , 
		 * 
		 * */
		//imageStdyind=factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+imaref.getUniqueIdentifier()));
    	axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasImageSerie,imageStdyind,imageSeries));
		
		
	}
	void imagestudyIndividuals(OWLOntologyManager m,OWLOntology o,Set<OWLAxiom> axioms,DicomImageReferencedE imaref,OWLNamedIndividual ind) throws IllegalAccessException, IntrospectionException
	{
		String IRIontology = o.getOntologyID().getOntologyIRI().toString();
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls ;
		OWLDataProperty hasproperty;
		PrefixManager pm = new DefaultPrefixManager(o.getOntologyID().getOntologyIRI().toString()+"#");
		OWLLiteral literal;
		
		
		OWLObjectProperty hasImageStudys = factory.getOWLObjectProperty(IRI
                .create(IRIontology + "#hasImageStudy"));
		
		
		//relation 1 imagereferenced to 1 imageStudy
		ImageStudy imgStdy= imaref.getImagestudy();	
		//System.out.println(imgStdy.getInstanceUid());
		
		/**
		******************************
									**/
		if(tempLesion.containsKey(imgStdy.getInstanceUid()))
		{tempLesion.get(imgStdy.getInstanceUid()).add(imaref.getUniqueIdentifier());}else
		{
			List<String> ls= new ArrayList<String>();
			ls.add(imaref.getUniqueIdentifier());
			tempLesion.put(imgStdy.getInstanceUid(),ls);
		}
		
		
		
		
		cls=m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#ImageStudy"));
		OWLNamedIndividual imageStdyind = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+imgStdy.getInstanceUid()));
		axioms.add(factory.getOWLClassAssertionAxiom(cls,imageStdyind));
		
		//Boolean tem=new Boolean("true");
		
		BeanInfo beanInfo = Introspector.getBeanInfo(imgStdy.getClass());
		
		// set all dataProperties of individuals 
		//
		for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
		    // get the name of class property
			String propertyName = propertyDesc.getName();
			
			
		    try {
		    	//get value of property
				Object value = propertyDesc.getReadMethod().invoke(imgStdy);
				System.out.println(propertyName);
				System.out.println(value);
				if(value != null ){
					if(!propertyName.equals("class") && !propertyName.equals("metaClass") && !propertyName.endsWith("Collection") && !propertyName.equals("imageSeries"))
					{
						//creating ... dataProperty into ontology
						//System.out.println(propertyName);
						//System.out.println(value);
						hasproperty = factory.getOWLDataProperty(propertyName, pm);
						//literal=(value.getClass() == tem.getClass())?factory.getOWLLiteral(value.toString(),booleans):factory.getOWLLiteral((String)value,literalDatatype);;
						
						literal=factory.getOWLLiteral(value.toString(),typess.get(value.getClass()));
						axioms.add(factory.getOWLDataPropertyAssertionAxiom(hasproperty,imageStdyind,literal));
					
					}
					if(propertyName.equals("imageSeries"))
					{	//System.out.println("entreeeeaaaa");
						imageSeriesIndividuals(m,o,axioms,imgStdy,imageStdyind);
						//imagestudyIndividuals(m,o,axioms,imarefindvDicom,ind);
						//spatialcoordinatecollectIndividuals(m, o, imarefindv,imarefindv.getType());
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
		System.out.println("*******************");
		
		
		//setDataProperties(markindv, cls, axioms, factory, o, ind);
		
		//ImageSeries imgSrie = imgStdy.getImageSeries();
		
		/**
		 * adding OBjectproperties , 
		 * 
		 * */
		//imageStdyind=factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+imaref.getUniqueIdentifier()));
    	axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasImageStudys,ind,imageStdyind));
    	
    	//System.out.println(tempLesion);
	
	}
	void imageReferencedIndividuals(OWLOntologyManager m,OWLOntology o,List<AnnotationsAIM4> ao,File fileformated) throws IntrospectionException, IllegalAccessException
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
				List<ImageReferenceEntity> markelements= (List<ImageReferenceEntity>) ann.getImagereferencedCollection();
				
				//System.out.println(markelements.size());
				for(ImageReferenceEntity imarefindv:markelements)
				{
					//case where a single lesion is deteccted in the code
					cls=m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#"+imarefindv.getType()));
					
					PrefixManager pm = new DefaultPrefixManager(o.getOntologyID().getOntologyIRI().toString()+"#");
					
					OWLDataProperty hasproperty;
					OWLLiteral literal; 
					
					
					if(imarefindv.getType().equals("DicomImageReferenceEntity"))
					{int count=0;
						
					DicomImageReferencedE imarefindvDicom = (DicomImageReferencedE)imarefindv;
							
					OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+imarefindvDicom.getUniqueIdentifier()));
					axioms.add(factory.getOWLClassAssertionAxiom(cls,ind));
					
					//Boolean tem=new Boolean("true");
					
					BeanInfo beanInfo = Introspector.getBeanInfo(imarefindvDicom.getClass());
					
					// set all dataProperties of markupentity individuals 
					for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
					    // get the name of class property
						String propertyName = propertyDesc.getName();
						//
					    try {
					    	//get value of property
							Object value = propertyDesc.getReadMethod().invoke(imarefindvDicom);
							if(value != null ){
								if(!propertyName.equals("class") && !propertyName.equals("metaClass") && !propertyName.endsWith("Collection") && !propertyName.equals("imagestudy"))
								{
									//creating ... dataProperty into ontology
									hasproperty = factory.getOWLDataProperty(propertyName, pm);
									//literal=(value.getClass() == tem.getClass())?factory.getOWLLiteral(value.toString(),booleans):factory.getOWLLiteral((String)value,literalDatatype);;
									
									literal=factory.getOWLLiteral(value.toString(),typess.get(value.getClass()));
									axioms.add(factory.getOWLDataPropertyAssertionAxiom(hasproperty,ind,literal));
								
								}
								if(propertyName.equals("imagestudy"))
								{	count++;
									imagestudyIndividuals(m,o,axioms,imarefindvDicom,ind);
									//spatialcoordinatecollectIndividuals(m, o, imarefindv,imarefindv.getType());
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
			      // System.out.println(count);
					
					/**
					 * adding OBjectproperties , 
					 * 
					 * */
					
									    
				  /*  if(imarefindv.getType().equals("TwoDimensionMultiPoint"))
					{	
						twoDimensionType tempp= (twoDimensionType) imarefindv; 
						for(twoDSCCollection collectt :(List<twoDSCCollection>)tempp.getTwoDspatialCoordinateCollection())
						{
							SpatialCoordinateind=factory.getOWLNamedIndividual(IRI.create(IRIontology+"#"+imarefindv.getUniqueIdentifier()+collectt.getCoordinateIndex()));
					    	
					    	axioms.add(factory.getOWLObjectPropertyAssertionAxiom(hasSpatialCoordinate,ind,SpatialCoordinateind));
											
						}
					}*/
					
					
					
				    
				    				
					
				}
					
					
				}
				
				
			}
		    
		    
			
		}
				
		m.addAxioms(o, axioms);
		axioms.clear();
	
	
	}
	
	void setModifiesImageStudy(OWLOntologyManager m,OWLOntology o,File fileformated) throws OWLOntologyStorageException
	{
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls ;
		PrefixManager pm = new DefaultPrefixManager(o.getOntologyID().getOntologyIRI().toString()+"#");
		
		OWLDataProperty hasproperty;
		OWLLiteral literal;
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		Boolean bl=true;
		cls=m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#ImageStudy"));
		for(Map.Entry<String, List<String> > entry: tempLesion.entrySet())
		{
			if(entry.getValue().size()==1)
			{
				OWLNamedIndividual ind = 
						factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+entry.getKey()));
				
				hasproperty = factory.getOWLDataProperty("singleLesion", pm);
				//literal=(value.getClass() == tem.getClass())?factory.getOWLLiteral(value.toString(),booleans):factory.getOWLLiteral((String)value,literalDatatype);;
				
				literal=factory.getOWLLiteral(bl.toString(),typess.get(Boolean.class));
				axioms.add(factory.getOWLDataPropertyAssertionAxiom(hasproperty,ind,literal));
				//axioms.add(factory.getOWLClassAssertionAxiom(cls,ind));
			}
		}
		
		m.addAxioms(o, axioms);
		axioms.clear();
		OWLOntologyFormat format = m.getOntologyFormat(o);
		
		m.saveOntology(o,format,IRI.create(fileformated.toURI()));
		
	}
}
