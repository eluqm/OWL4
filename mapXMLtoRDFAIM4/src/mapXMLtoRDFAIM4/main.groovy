
package mapXMLtoRDFAIM4;

import de.datenwissen.util.groovyrdf.jena.JenaRdfBuilder
import groovy.io.FileType

/*class HelloWorld {
def name
def greet() { "Hello ${name}" }
}
*/
def list=[]

def dir = new File("/Users/edson/OWL4/annotations")
dir.eachFileRecurse(FileType.FILES) {
				file->list << file
	}
def num=0
def aimfile
list.each {
	if(it.name.endsWith('.xml'))
	{
		println it.path
		aimfile = new XmlSlurper().parse(new File(it.path.toString()))
		if(aimfile.empty){println "problema,,,"}
		aimfile.person.each {
			println "${it.@name}"
		}
	}
	
}
println num

def aimfile2 = new XmlSlurper().parse(new File("/Users/edson/OWL4/annotations/12yo41am1275kqwxswcvxifmasy4l04bvwe92mvu.xml"))
if(aimfile2.empty){println "problema,,,"}
println aimfile2.person

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
println list2.car.country


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



