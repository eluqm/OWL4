package staging;

import java.beans.IntrospectionException;
import java.io.File;

import org.codehaus.groovy.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
//import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.HashCode;
import org.semanticweb.owlapi.vocab.OWL2Datatype;









import com.clarkparsia.modularity.PelletIncremantalReasonerFactory;
import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
//import com.clarkparsia.protege.plugin.pellet.PelletReasonerFactory;




import extractor.Extractor;

//import uk.ac.manchester.cs.owl.owlapi.OWLOntologyBuilderImpl;



public class Staging {
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, IntrospectionException, IllegalAccessException {
        //System.out.println("Hello, World!");
        
        // loading the ontology
		String pathlinux = "/home/edson/Documentos/OWL4/owl4versions"; 
        File file=null; 
        
        // file to save ontology + individuals
        String pathmac="/Users/edson/OWL4/owl4versions";
        String pathwindows="C:\\OWL4\\owl4versions";
        File fileformated=null;
       // File fileformated = new File("/home/edson/Documentos/OWL4/OWL4/AIM4ind.owl");
        
        if (System.getProperty("os.name").toLowerCase().contains("linux")){
			
        	file = new File(pathlinux + "/AIM4.owl");
        	fileformated = new File( pathlinux + "/AIM4ind4.owl");
        } else {
			if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			
				file = new File(pathmac + "/AIM4.owl");
	        	fileformated = new File( pathmac + "/AIM4ind4.owl");
			}
			else{
				if (System.getProperty("os.name").toLowerCase().contains("windows")) {
					
					file = new File(pathwindows + "/AIM4.owl");
		        	fileformated = new File( pathwindows + "/AIM4ind5.owl");
				}
				
			}
			
		}
        
        
        
        System.out.println("empezando el codigo++");
        
        
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        OWLOntology o = m.loadOntologyFromOntologyDocument(file);
     
	    
	        //OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	        //OWLOntology localOntology = manager.loadOntologyFromOntologyDocument(file);
 
	        // getting all axioms    
	        Set<OWLAxiom> axSet= o.getAxioms();
 
			//System.out.println(o.getLogicalAxiomCount());
			//System.out.println("termino carga onto");
			System.out.println("++++++++++++++++++++");
			
			// Get Thing
			//OWLClass clazz = o.getOWLThing();
			//System.out.println("Class : " + clazz);
			// Print the hierarchy below thing
			//printHierarchy(o, clazz, new HashSet<OWLClass>());
			
			
			// prepare ontology and reasoner 
	        PelletReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
	        System.out.println("2 ++++++++++++++++++++");
			PelletReasoner reasoner = reasonerFactory.createReasoner(o); 
			
			//OWLReasoner reasoner2 = reasonerfac.
			//reasoner.getKB().realize();
			
			// print tree class
			//reasoner.getKB().printClassTree();
			
			
			//IRI documentIRI = IRI.create(inputOntology);
			System.out.println("Ontology Loaded...");
			
	        System.out.println("Logical URI : " + OWL2Datatype.XSD_INTEGER.getIRI());
	        System.out.println("Document IRI: " + o.getOntologyID().getOntologyIRI());
	        System.out.println("Format      : "+ m.getOntologyFormat(o)); 
	        OWLOntologyFormat format = m.getOntologyFormat(o);
	        System.out.println("++++++++++++++++++++");
	        // parsing all annotation files
	        
	        parserAIMFILES par= new parserAIMFILES();
			par.readAnnotationsFiles();
			par.fillAnnotationsClass();
			List<AnnotationsAIM4> ao=(List<AnnotationsAIM4>) par.getLs();
	        
			for(AnnotationsAIM4 ab:ao)
			{
				for(Annotation a:(List<Annotation>) ab.getImageAnnotations())
				{
					List<Double> xs= new ArrayList<Double>() ;
					List<Double> ys= new ArrayList<Double>();
					
					double resp=0;
					
					for(CalculationEntity aa:(List<CalculationEntity>) a.getCalculationEntityCollection())
					{
						
						
						for(ExtendedCalculationResult aaa:(List<ExtendedCalculationResult>) aa.getCalculationResultCollection())
						{
							
							for(CalculationData aaaa:(List<CalculationData>) aaa.getCalculationDataCollection())
							{
								resp=(double) aaaa.getValues();
								
							}
							
						}
						
						
						/*
						
						for(MarkupEntity bb:(List<MarkupEntity>) aa.getMarkupEntityCollection())
						{
							bb.;
							
						}*/
						
					}
					for(twoDimensionGeometricShapeEntity mark:(List<twoDimensionGeometricShapeEntity>)a.getMarkupEntityCollection())
					{
						for(twoDSCCollection corrd:	(List<twoDSCCollection>)mark.getTwoDspatialCoordinateCollection())
						{
							xs.add((double) corrd.getX());
							ys.add((double) corrd.getY());
							
						}
					}
					
					double spacin = Extractor.calculateLineLength(xs.get(0),xs.get(1),ys.get(0),ys.get(1),resp);
					double resps=Extractor.calculateLineLength2(xs.get(0),xs.get(1),ys.get(0),ys.get(1),resp);
					/*System.out.println("possible pixelspacing :"+spacin);
					System.out.println("distancia en pixeles :"+ resps);
					System.out.println("valor en cm :" + resps*spacin);
					System.out.println("valor en cm real :" + resp);
					System.out.println("____________________");*/
					xs.clear();
					ys.clear();
					
				}
				
			}
		  
			
	        //creating person individuals 
			SetIndividuals individuals = new SetIndividuals(m);
			try {
				individuals.markupIndividuals(m, o, ao, fileformated);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			individuals.imageReferencedIndividuals(m,o,ao,fileformated);
			individuals.calculationEntityCollection(m, o, ao, fileformated);
			individuals.imagingObservationentityIndividuals(m, o, ao, fileformated);
			individuals.imagingphysicalentityIndividuals(m, o, ao, fileformated);
			individuals.imageannotationscollectIndividuals(m, o, ao, fileformated);
			individuals.personIndividuals(m,o,ao,fileformated);
			individuals.annotationIndividuals(m, o, ao, fileformated);
			
			
			
			
			NodeSet<OWLNamedIndividual> instances;
			for (OWLClass c : o.getClassesInSignature()) {
		       instances = reasoner.getInstances(c, true);
		       //System.out.println(c.getIRI().getFragment());
		        if (c.getIRI().getFragment().equals("Person")){
		        	System.out.println("person clas...");
		            for (OWLNamedIndividual i : instances.getFlattened()) {
		                System.out.println(i.getIRI().getFragment()); 

		        }}
		        
		       

		    }
			
			
			
			//reasoner.getKB().realize();
			//reasoner.getKB().printClassTree();
			//m.saveOntology(o,format,IRI.create(fileformated.toURI()));
			if(reasoner.isConsistent()){
			System.out.println("terminooo y es consistente la ontologia");
			}
			
			par.printAnnotations();
			
			String namepat="GL-1-160-282690^^^^";
			//System.out.println(ao.size());
			for (AnnotationsAIM4 element : ao) {
				//System.out.println(element.getPerson().getName());
				if( element.getPerson().getName().equals(namepat) )
				{
					//System.out.println(element.getUid());
				for(Annotation	ann: (List<Annotation>)element.getImageAnnotations())
				{
					for(CalculationEntity anncal:(List<CalculationEntity>)ann.getCalculationEntityCollection())
					{
						//List<CalculationData> li=(List<CalculationData>) ((List<calculationResult>)anncal.getCalculationResultCollection()).get(0).getCalculationDataCollection();
						//System.out.println(li.get(0).getValue());
					}
				}
				}
				
			}
			//AnnotationsAIM4 as = new AnnotationsAIM4();
			
			//List<ImagingPhysicalEntity> lill=((List<ImagingPhysicalEntity>)((List<Annotation>)((AnnotationsAIM4) ao.get(0)).getImageAnnotations()).get(0).getImagingPhysicalEntityCollection());
			
			//List<HashMap<String,String> > lill2=(List<HashMap<String, String>>) lill.get(0).getTypeCode();
			//System.out.println(lill2.size());
			
			//par.printAnnotations();
			//System.out.println(par.getSetNumberPatients2());
			
			 
        //OWLOntologyBuilderImpl format = (OWLOntologyBuilderImpl) m.getOntologyFormat(o); 
         
        //System.out.println();
        
    }

}
