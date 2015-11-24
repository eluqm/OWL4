package staging;

import java.io.File;
import java.util.Set;

import org.mindswap.pellet.utils.progress.ConsoleProgressMonitor;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.FreshEntityPolicy;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.ReasonerProgressMonitor;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

public class Reasoninng {
	public static void main(String[] args) throws OWLOntologyCreationException 
	{
		
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
		        	fileformated = new File( pathwindows + "/AIM4ind4.owl");
				}
				
			}
			
		}
        
        
        
System.out.println("starting...");
        
//the reasoner progress out to the console.
ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
// Specify the progress monitor via a configuration. We could also
// specify other setup parameters in the configuration, and different
// reasoners may accept their own defined parameters this way.
//OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);

        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        OWLOntology o = m.loadOntologyFromOntologyDocument(fileformated);
     
	    
        
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
	        System.out.println("preparing reasoner ++++++++++++++++++++");
			PelletReasoner reasoner = reasonerFactory.createReasoner(o);
			
			//OWLReasoner reasoner2 = reasonerfac.
			//reasoner.getKB().realize();
			reasoner.precomputeInferences();
			Reasoninng sr=new Reasoninng();
			String src="tnm3";
			sr.ShowIndividualsClass(src, o, reasoner);
			
			 System.out.println("finishied ++++++++++++++++++++");
        
        
	}
	
	public void ShowIndividualsClass(String Nameclass,OWLOntology o,PelletReasoner reasoner)
	{
		System.out.println("....");
		NodeSet<OWLNamedIndividual> instances;
		for (OWLClass c : o.getClassesInSignature()) {
	       instances = reasoner.getInstances(c, true);
	       //System.out.println(c.getIRI().getFragment());
	        if (c.getIRI().getFragment().equals(Nameclass)){
	        	System.out.println(Nameclass+" clas...");
	            for (OWLNamedIndividual i : instances.getFlattened()) {
	                System.out.println(i.getIRI().getFragment()); 

	        }}
	        
	       

	    }
		
	}
	}
