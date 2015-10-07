package checkProfiles;

import java.io.File;

import javax.swing.text.BadLocationException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.UnknownOWLOntologyException;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWL2ELProfile;
import org.semanticweb.owlapi.profiles.OWL2QLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;

public class CheckProfileOntology {
	
	File file;
	private OWLOntology loadOntology() throws OWLOntologyCreationException {
    	
		// Get hold of an ontology manager
		String pathlinux = "/home/edson/Downloads"; 
        //File file=null; 
        
        // file to save ontology + individuals
        String pathmac="/Users/edson/OWL4/cancerStaging1/RadLex_OWL";
        //File fileformated=null;
       // File fileformated = new File("/home/edson/Documentos/OWL4/OWL4/AIM4ind.owl");
        
        if (System.getProperty("os.name").toLowerCase().contains("linux")){
			
        	file = new File(pathlinux + "/owlapi.xrdf");
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
	
	private void checkProfileEL () throws BadLocationException, OWLOntologyCreationException {
		OWLOntology ontology= this.loadOntology();
		OWL2ELProfile profileEL = new OWL2ELProfile();
		OWLProfileReport reportEL = profileEL.checkOntology(ontology);
		
		//areaEL.setText("");
		//StyleContext sc = StyleContext.getDefaultStyleContext(); 
	    //AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, java.awt.Color.BLACK);
		//StyledDocument doc = areaEL.getStyledDocument();
		
		if (reportEL.isInProfile()) {
			System.out.println("YES");
		} else {
			System.out.println("NO");
			for (OWLProfileViolation v : reportEL.getViolations()) {
				//System.out.println(v.toString() + "\n\n");
			}
		}
	}
    
    private void checkProfileQL () throws BadLocationException, OWLOntologyCreationException {
    	OWLOntology ontology= this.loadOntology();
    	OWL2QLProfile profileQL = new OWL2QLProfile();
		OWLProfileReport reportQL = profileQL.checkOntology(ontology);
		
		if (reportQL.isInProfile()) {
			System.out.println("YES");
		} else {
			System.out.println("NO");
			for (OWLProfileViolation v : reportQL.getViolations()) {
				//System.out.println(v.toString() + "\n\n");
			}
		}
	}
	
	public static void main(String [] args) throws OWLOntologyCreationException, BadLocationException
	{
		CheckProfileOntology check = new CheckProfileOntology();
		//check.loadOntology();
		check.checkProfileDL();
		//check.checkProfileEL();
		//check.checkProfileQL();
		
		
	}
	
}
