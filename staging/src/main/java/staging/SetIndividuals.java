package staging;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.util.ArrayList;
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
		
		
		
		/*PrefixManager pm = new DefaultPrefixManager(o.getOntologyID().getOntologyIRI().toString());
		OWLDataProperty hasAge = factory.getOWLDataProperty("age", pm);
		OWLDatatype integerDatatype = factory
                .getOWLDatatype(OWL2Datatype.XSD_INT.getIRI());
		
		*/
		
        // Create a typed literal. We type the literal "51" with the datatype
        //OWLLiteral literal = factory.getOWLLiteral("51", integerDatatype);
        
        // Create the property assertion and add it to the ontology
        //OWLAxiom ax = factory.getOWLDataPropertyAssertionAxiom(hasAge, ind,
        //        literal);
        //m.addAxiom(o, ax);
		
		//m.applyChange(addAxion);
		OWLOntologyFormat format = m.getOntologyFormat(o);
		
		m.saveOntology(o,format,IRI.create(fileformated.toURI()));
		
		//m.saveOntology(o, );
		
	}
	// create individuals of annotation class
	void annotationIndividuals(OWLOntologyManager m,OWLOntology o,List<AnnotationsAIM4> ao,File fileformated) throws OWLOntologyStorageException
	{
		String IRIontology = o.getOntologyID().getOntologyIRI().toString();
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls = m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#ImageAnnotation"));
		
		// object properties from annotationEntity
		OWLObjectProperty hasmarkup = factory.getOWLObjectProperty(IRI.create(IRIontology + "#hasMarkupEntity"));
		
		// iterate over all patient annotation 
		for (AnnotationsAIM4 element : ao) {
			List<Annotation> elements= (List<Annotation>) element.getImageAnnotations();
			for(Annotation ann: elements)
			{
				
				
				OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+ann.getUniqueIdentifier().toString()));
				OWLAxiom axiom = factory.getOWLClassAssertionAxiom(cls,ind);
				AddAxiom addAxion = new AddAxiom(o, axiom);
				m.applyChange(addAxion);
				
				//add data properties
				PrefixManager pm = new DefaultPrefixManager(o.getOntologyID().getOntologyIRI().toString()+"#");
				OWLDataProperty hasname = factory.getOWLDataProperty("name", pm);
				OWLDatatype literalDatatype = factory
		                .getOWLDatatype(OWL2Datatype.RDFS_LITERAL.getIRI());
							
				// Create a typed literal. We type the literal "nameofAnnotation" with the datatype
		        OWLLiteral literal = factory.getOWLLiteral(ann.getName(), literalDatatype);
		        
		        // Create the property assertion and add it to the ontology
		        OWLAxiom ax = factory.getOWLDataPropertyAssertionAxiom(hasname, ind,
		                literal);
		        m.addAxiom(o, ax);
				
				m.applyChange(addAxion);
				
				
			}
		    
		    
			
		}
		//m.applyChange(addAxion);
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
		    //set axiom 
		    axioms.add(factory.getOWLClassAssertionAxiom(cls,ind));
		    OWLIndividual indannotation;
		    
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
		    
		    // set objectproperty hasmarkup domain: class imageannotationcollection range: MarkupEntity
		   
		
		}
		
		 
			
			m.addAxioms(o, axioms);
			axioms.clear();
		
		
	}
	void markupIndividuals(OWLOntologyManager m,OWLOntology o,List<AnnotationsAIM4> ao,File fileformated) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IntrospectionException
	{
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls ;
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
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
					OWLDatatype literalDatatype = factory
			                .getOWLDatatype(OWL2Datatype.RDFS_LITERAL.getIRI());
					OWLDatatype booleans = factory.getBooleanOWLDatatype();
					OWLDataProperty hasproperty;
					OWLLiteral literal; 
					
					
					OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+markindv.getUniqueIdentifier()));
					axioms.add(factory.getOWLClassAssertionAxiom(cls,ind));
					
					Boolean tem=new Boolean("true");
					
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
									literal=(value.getClass() == tem.getClass())?factory.getOWLLiteral(value.toString(),booleans):factory.getOWLLiteral((String)value,literalDatatype);;
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
		OWLClass cls ;
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		//List<twoDSCCollection> tempp = ao.;
		
		if(tip.equals("TwoDimensionMultiPoint"))
		{	System.out.println("entro a coordenadas");
			twoDimensionType tempp= (twoDimensionType) ao; 
			for(twoDSCCollection collectt :(List<twoDSCCollection>)tempp.getTwoDspatialCoordinateCollection())
			{
				
				cls=m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#TwoDimensionSpatialCoordinate"));
				
				PrefixManager pm = new DefaultPrefixManager(o.getOntologyID().getOntologyIRI().toString()+"#");
				OWLDatatype literalDatatype = factory
		                .getOWLDatatype(OWL2Datatype.RDFS_LITERAL.getIRI());
				OWLDatatype booleans = factory.getBooleanOWLDatatype();
				
				OWLDataProperty hasproperty;
				
				OWLLiteral literal;
				OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(o.getOntologyID().getOntologyIRI()+"#"+ao.getUniqueIdentifier()+collectt.getCoordinateIndex()));
				axioms.add(factory.getOWLClassAssertionAxiom(cls,ind));
				
				Boolean tem=new Boolean("true");
				
				BeanInfo beanInfo = Introspector.getBeanInfo(collectt.getClass());
				
				for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
				    // get the name of class property
					String propertyName = propertyDesc.getName();
				    try {
				    	//get value of property
						Object value = propertyDesc.getReadMethod().invoke(collectt);
						if(value != null ){
							if(!propertyName.equals("class") && !propertyName.equals("metaClass") && !propertyName.endsWith("Collection"))
							{
								//creating ... dataProperty into ontology
								hasproperty = factory.getOWLDataProperty(propertyName, pm);
								literal=(value.getClass() == tem.getClass())?factory.getOWLLiteral(value.toString(),booleans):factory.getOWLLiteral((String)value,literalDatatype);;
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
		}
		m.addAxioms(o, axioms);
		axioms.clear();
	}
	

	

}
