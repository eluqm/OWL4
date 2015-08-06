
//package mapXMLtoRDFAIM4;

import de.datenwissen.util.groovyrdf.jena.JenaRdfBuilder
import groovy.io.FileType

/*class HelloWorld {
def name
def greet() { "Hello ${name}" }
}
*/
def list=[]
//def so= System.properties['os.name']
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
list.each {
	if(it.name.endsWith('.xml'))
	{
		//println it.path
		aimfile = new XmlSlurper().parse(new File(it.path.toString()+""))
		if(aimfile.empty){
			println "problema al abrir el documento"
			
		}
	}
		//aimfile.person.each {
		//	println "${it.@name}"
		//}
	}
	

//def alert = com.eviware.soapui.support.UISupport;
//Define a file pointer for groovy to handle the file operations.
def inputFile = new File("/home/edson/Documentos/OWL4/OWL4/annotations/12yo41am1275kqwxswcvxifmasy4l04bvwe92mvu.xml")
String adds="/home/edson/Documentos/OWL4/OWL4/annotations/12yo41am1275kqwxswcvxifmasy4l04bvwe92mvu.xml"
//String file = "C:\\Documents and Settings\\user\\Desktop\\create.xml";
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
		println it.name()
	}
	

		
	
	//assert InputXML2.person.name.text() == 'VAG-1-385-319433^^^^'
	
	def mapp=[:]
	mapp=InputXML2.children()
	println mapp
	
}


def text = '''
    <records>
    <car name='HSV Maloo' make='Holden' year='2006'>
      <country>Australia</country>
      <record type='speed'>Production Pickup Truck with speed of 271kph</record>
    </car>
    <car name='P50' make='Peel' year='1962'>
      <country>Isle of Man</country>
      <record type='size'>Smallest Street-Legal Car at 99cm wide and 59 kg in weight</record>
    </car>
    <car name='Royale' make='Bugatti' year='1931'>
      <country>France</country>
      <record type='price'>Most Valuable Car at $15 million</record>
    </car>
  </records>
'''

def list2 = new XmlSlurper().parseText(text)

assert list2 instanceof groovy.util.slurpersupport.GPathResult
println list2.car[2]


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
*/



