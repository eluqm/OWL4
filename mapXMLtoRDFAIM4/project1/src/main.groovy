
//package mapXMLtoRDFAIM4;
//import org.apache.jena.rdf.model.ModelFactory;
@Grab('com.github.albaker:GroovySparql:0.9.0')
//@Grab(group='org.codehaus.groovy.modules.http-builder',module='http-builder', version='0.5.0')
import org.codehaus.groovy.control.customizers.ImportCustomizer.Import;

import de.datenwissen.util.groovyrdf.jena.JenaRdfBuilder
import de.datenwissen.util.groovyrdf.jena.JenaRdfLoader
import groovy.io.FileType
import de.datenwissen.util.groovyrdf.core.RdfData;
import de.datenwissen.util.groovyrdf.core.RdfDataFormat;
import de.datenwissen.util.groovyrdf.core.RdfNamespace;
import de.datenwissen.util.groovyrdf.core.RdfBuilder;
import org.apache.jena.rdf.model.*;




import groovy.sparql.*




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


/** SPARQL 1.0 or 1.1 endpoint
 ******************************/
def sparql = new Sparql(endpoint:"http://dbpedia.org/sparql", user:"user", pass:"pass")

def query = "SELECT ?s ?p ?o WHERE { ?s ?p ?o } LIMIT 4"

// sparql result variables projected into the closure delegate
sparql.each query, {
	println "${s} : ${p} : ${o}"
}

def builder = new RDFBuilder()
        //[xml:"RDF/XML", xmlabbrev:"RDF/XML-ABBREV", ntriple:"N-TRIPLE", n3:"N3", turtle:"TURTLE"]
        def output = builder.model {
            defaultNamespace "urn:test"
            namespace ns1:"urn:test1"
            subject("#joe") {
                predicate "ns1:name":"joe"
            }

        }

        println output
		
// rdf/xml form
		BufferedOutputStream out
		PipedInputStream pipeInput
		pipeInput = new PipedInputStream();
		reader = new BufferedReader(new InputStreamReader(pipeInput));
		out = new BufferedOutputStream(new PipedOutputStream(pipeInput));
		
	
		def builder2 = new RDFBuilder(out)
			//[xml:"RDF/XML", xmlabbrev:"RDF/XML-ABBREV", ntriple:"N-TRIPLE", n3:"N3", turtle:"TURTLE"]
			def output2 = builder2.xml {
				defaultNamespace "urn:test"
				namespace ns1:"urn:test1"
				subject("#joe") {
				   predicate "ns1:name":"joe"
				}
				
			}
			String s=null
			while ((s=reader.readLine())!=null){println s}
			

//RdfData rdfData3 = rdfLoader.load('http://dbpedia.org/page/Grateful_Dead')
//println rdfData2



