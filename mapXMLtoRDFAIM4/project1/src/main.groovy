
//package mapXMLtoRDFAIM4;
//import org.apache.jena.rdf.model.ModelFactory;
import org.codehaus.groovy.control.customizers.ImportCustomizer.Import;

import de.datenwissen.util.groovyrdf.jena.JenaRdfBuilder
import de.datenwissen.util.groovyrdf.jena.JenaRdfLoader
import groovy.io.FileType
import de.datenwissen.util.groovyrdf.core.RdfData;
import de.datenwissen.util.groovyrdf.core.RdfDataFormat;
import de.datenwissen.util.groovyrdf.core.RdfNamespace;
import de.datenwissen.util.groovyrdf.core.RdfBuilder;





def list=[]
def pathLinux="/home/edson/Documentos/OWL4/OWL4/annotations"
def pathmac="/Users/edson/OWL4/annotations"
def dir
if (System.properties['os.name'].toLowerCase().contains('linux')) {
	println "it's linux"
	dir = new File(pathLinux)
} else {
	if (System.properties['os.name'].toLowerCase().contains('mac')) {
		println "it's mac"
		dir = new File(pathmac)
} 	else {
	}
	println "it's  Windows"
}

dir.eachFileRecurse(FileType.FILES) {
				file->list << file
	}

def num=0
def aimfile
StringBuilder sb = new StringBuilder()
BufferedReader br
def InputXML2
String temp

// read number of patients and annotations
def setNumberPatients = new HashSet();
def listID
def setNumberPatients2 = new HashMap<String,List<String>>();

list.each {
	if(it.name.endsWith('.xml'))
	{   num++
		
		br = new BufferedReader(new InputStreamReader(new FileInputStream(it.path)));
		while ((line = br.readLine()) !=null) {
			sb.append(line);
		}
		
		//println "sb.toString() = " + sb.toString()
		
		InputXML2= new XmlSlurper().parseText(sb.toString())
		temp=InputXML2.person.name.'@value'
		temp2=InputXML2.uniqueIdentifier.'@root'
		
		def found = setNumberPatients2.find { key, value -> key == temp }
		
		if(found != null ){
				setNumberPatients2[temp].add(temp2)
		}else{
		def ins= new ArrayList<String>()
		ins.add(temp2)
		setNumberPatients2.put(temp,ins)
		}
		
				
		sb.setLength(0)
	}
}

println num
setNumberPatients2.each{ k, v -> println "${k}:${v}" }
println setNumberPatients2.size()

def vocab = new RdfNamespace("http://example.com/vocab/")
vocab.anything == "http://example.com/vocab/anything"




//def m= ModelFactory.createDefaultModel();
println vocab.anything

def rdfBuilder = new JenaRdfBuilder()
RdfData rdfData = rdfBuilder {
	"http://example.com/resource/alice" {
	  "http://example.com/vocab/name" "Alice"
	}
  }
  
def names = ["Alice", "Julia"]

RdfData rdfData2 = rdfBuilder {
  "http://example.com/resource/alice" {
     a "http://example.com/vocab/Person"
    "http://example.com/vocab/name" names
  }
}
def rdfLoader = new JenaRdfLoader()

RdfData rdfData3 = rdfLoader.load('http://purl.org/dc/elements/1.1/type')
println rdfData2



