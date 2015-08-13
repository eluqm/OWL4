
//package mapXMLtoRDFAIM4;

import de.datenwissen.util.groovyrdf.jena.JenaRdfBuilder
import groovy.io.FileType


import de.datenwissen.util.groovyrdf.core.RdfData;
import de.datenwissen.util.groovyrdf.core.RdfDataFormat;
import de.datenwissen.util.groovyrdf.core.RdfNamespace;
import de.datenwissen.util.groovyrdf.core.RdfBuilder;
/*class HelloWorld {
def name
def greet() { "Hello ${name}" }
}
*/
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
		
		/*if(!inputFile.exists())
		{
			//Display an alert if the file is not found.
			alert.showInfoMessage("Input File 'DATASHEET.xml' not found!");
		}
		else
		{
			
		}
		*/
		
		
		sb.setLength(0)
	}
}

println num
setNumberPatients2.each{ k, v -> println "${k}:${v}" }
println setNumberPatients2.size()

def vocab = new RdfNamespace("http://example.com/vocab/")
vocab.anything == "http://example.com/vocab/anything"
rdfBuilder = new JenaRdfBuilder ()

/*
//Define a file pointer for groovy to handle the file operations.
def inputFile = new File("/Users/edson/OWL4/annotations/12yo41am1275kqwxswcvxifmasy4l04bvwe92mvu.xml")
String adds="/Users/edson/OWL4/annotations/12yo41am1275kqwxswcvxifmasy4l04bvwe92mvu.xml"
String line = "";
StringBuilder sb = new StringBuilder();
BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(adds)));
while ((line = br.readLine()) !=null) {
	sb.append(line);
}
println "sb.toString() = " + sb.toString()

if(!inputFile.exists())
{
	//Display an alert if the file is not found.
	alert.showInfoMessage("Input File 'DATASHEET.xml' not found!");
}
else
{
	//Read and parse XML file and store it into a variable
	def InputXML = new XmlParser().parseText(inputFile.text)
	def InputXML2 = new XmlSlurper().parseText(sb.toString())
	assert InputXML2 instanceof groovy.util.slurpersupport.GPathResult
	//Find/ Filter XML nodes based on a condition
	//def InputRow = InputXML.Employee.findAll{lkklkl
	//	it.Age.text().toInteger() > 19;
		//We are finding all XML nodes where the age > 19
	//}
	//InputRow.each{
			//Display the value of name node from the filtered record
	//		log.info(it.Name.text());
	//	}
	//println InputXML.name[0].@value
	InputXML.children().each { 
		def mapval=[:]
		mapval= it.attributes()
		println mapval
		println mapval.size()
		}
	
	println InputXML.name.size()
	if(InputXML2.empty){
		println "problema al abrir el documento"
		//println InputXML2.
		
	}
	InputXML2.children().each {
		println it.text()
	}
	
	
	InputXML2.'**'.findAll {it.name() == 'imageAnnotations'}.each{
		
		println it
		
	}
	def mas
	 mas= InputXML2.imageAnnotations.ImageAnnotation.uniqueIdentifier.'@root'
	 println mas
		
	
	//assert InputXML2.person.name.text() == 'VAG-1-385-319433^^^^'
	
}*/



//println list2.car[2]


/*/Users/edson/OWL4/annotations/12yo41am1275kqwxswcvxifmasy4l04bvwe92mvu.xml*/





/*
def helloWorld = new HelloWorld()
helloWorld.name = "Groovy"
def pers=new XmlSlurper().parse(new File("/Users/edson/OWL4/modelAIM_v4_rv48/owl4_1.xsd"))
def rdfBuilder = new JenaRdfBuilder()

def temp=pers.depthFirst().findAll{it.name()}
if(temp != null){	
	print(temp)
}

aimfile = new XmlSlurper().parse(new File(it.path.toString()+""))
		if(aimfile.empty){
			//println "problema al abrir el documento"
			
		}
*/



