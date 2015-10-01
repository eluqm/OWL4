package checkProfiles;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.UnknownOWLOntologyException;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;

public class CheckProfileOntology {
	
	File file;
	private OWLOntology loadOntology() throws OWLOntologyCreationException {
    	
		// Get hold of an ontology manager
		String pathlinux = "/home/edson/Documentos/OWL4"; 
        //File file=null; 
        
        // file to save ontology + individuals
        String pathmac="/Users/edson/OWL4/cancerStaging1/RadLex_OWL";
        //File fileformated=null;
       // File fileformated = new File("/home/edson/Documentos/OWL4/OWL4/AIM4ind.owl");
        
        if (System.getProperty("os.name").toLowerCase().contains("linux")){
			
        	file = new File(pathlinux + "/SustenAgroOntology.rdf");
        	//fileformated = new File( pathlinux + "/AIM4ind.owl");
        } else {
			if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			
				file = new File(pathmac + "/Radlex_3.9.owl");
	        	//fileformated = new File( pathmac + "AIM4ind.owl");
			} 	
			
		}
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    	
		OWLOntologyManager modelManager = OWLManager.createOWLOntologyManager(); ;
		OWLOntology ontology = modelManager.loadOntologyFromOntologyDocument(file);
		
		System.out.println("Ontology Loaded...");
		System.out.println("Ontology: " + ontology.getOntologyID());
		try {
	        System.out.println("Format: " + manager.getOntologyFormat(ontology));
	        
		} catch (UnknownOWLOntologyException e) {
			System.out.println("Format: (error) " + e);
		}        
        return ontology;
	}
	private void checkProfileDL () throws OWLOntologyCreationException {
		OWLOntology ontology= this.loadOntology();
		OWL2DLProfile profileDL = new OWL2DLProfile();
		OWLProfileReport reportDL = profileDL.checkOntology(ontology);
		
		if (reportDL.isInProfile()) {
			System.out.println("YES");
		} else {
			System.out.println("NO");
			for (OWLProfileViolation v : reportDL.getViolations()) {
				System.out.println(v.toString() + "\n\n");
			}
		}
		
	}
	
	public static void main(String [] args) throws OWLOntologyCreationException
	{
		CheckProfileOntology check = new CheckProfileOntology();
		//check.loadOntology();
		check.checkProfileDL();
		
		
	}
	
}
