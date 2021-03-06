package cancerStaging1;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
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
		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass cls = m.getOWLDataFactory().getOWLClass(IRI.create(o.getOntologyID().getOntologyIRI() + "#ImageAnnotation"));

		// iterate over all patient annotation 
		for (AnnotationsAIM4 element : ao) {
			List<Annotation> elements= (List<Annotation>) element.getImageAnnotations();
			for(Annotation ann: elements)
			{
				
				//System.out.println("UID DE ANOOTATONS"+ann.getUniqueIdentifier());
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
		   
		
		}
		
		 
			
			m.addAxioms(o, axioms);
			axioms.clear();
		
		
	}
	void markupIndividuals()
	{
		
	}
	
	
	

}