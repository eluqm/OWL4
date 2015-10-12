package staging

import groovy.io.FileType

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.StringBuilder;
import java.text.SimpleDateFormat
import java.util.Formatter.DateTime

import org.omg.CORBA.TypeCode;

import groovy.transform.Field
import groovy.util.slurpersupport.NodeChildren;

class Test {
	static void main(String[] args) {
		def a= new parserAIMFILES();
		println 'pasoo'
		a.readAnnotationsFiles();
		println 'paso2'
		a.fillAnnotationsClass();
		println 'paso3'
		//println a.ls.size()
		//println a.ls[0].Uid
		//println a.ls[0].person.name
		//a.printAnnotations()
	}

}

/*class annotationCollection
 {
 int idUI
 String namePatient
 }*/
class Location {
	String street
	String city
	String state
	double latitude
	double longitude
}

class Person
{
	String id;

	String sex;

	String ethnicGroup;

	String name;

	Date birthDate;

	@Override
	public String toString()
	{
		return "ClassPojo [id = "+id+", sex = "+sex+", ethnicGroup = "+ethnicGroup+", name = "+name+", birthDate = "+birthDate+"]";
	}
}

class ImagingPhysicalEntity
{	// uniqueIdentifier from abstract class Entity
	String uniqueIdentifier
	//Coded entry data used to describe or capture an imaging physical entity = map string:string [0...n]
	def typeCode = new ArrayList<ArrayList<HashMap <String,String>>>()
	String annotatorConfidence
	String label
}

// acts  as imageAnnotationCollection=rootclass
class AnnotationsAIM4
{
	String Uid

	Person person

	//  imageannotations
	def imageAnnotations=new ArrayList<Annotation>()

}

class parserAIMFILES
{

	def list=[]
	def pathLinux="/home/edson/Documentos/OWL4/annotations"
	def pathmac="/Users/edson/OWL4/annotations"
	def dir
	def num=0
	def aimfile
	StringBuilder sb = new StringBuilder()
	BufferedReader br
	def InputXML2
	String temp
	String temp2

	// mapping all xml files such a list of AnnotationAIM4 classes
	def ls = new ArrayList<AnnotationsAIM4 >()


	def setNumberPatients = new HashSet();
	def listID
	def setNumberPatients2 = new HashMap<String,List<String>>();

	void readAnnotationsFiles()
	{
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

	}
	void fillPerson(Object y,AnnotationsAIM4 z)
	{
		//
		String tempp=y.uniqueIdentifier.'@root'

		// set uniqueIdentifier of annotation
		z.setUid(tempp);
		// get Person data
		def per = new Person()
		String names = y.person.name.'@value'
		names=names.replaceAll("\\^","")
		//println names
		per.setName(names)
		per.setId((String)y.person.id.'@value')
		per.setSex((String)y.person.sex.'@value')
		// add
		z.setPerson(per)


	}

	void fillAnnotationsStatement(Object y)
	{

		y.children().each {node ->
			//println node.name()

		}
	}
	void fillimagingPhysicalEntity(Object y, Annotation z)
	{
		//z.imagingPhysicalEntityCollection
		//println "entreeeeeeeee"
		def map = new ArrayList<ArrayList<HashMap<String,String>>>()


		y.children().each {node ->
			def imaginPhy = new ImagingPhysicalEntity()
			def map2=new ArrayList<HashMap<String,String>>()
			//println 'entro a ...'+node.name()
			imaginPhy.uniqueIdentifier=node.uniqueIdentifier.'@root'
			//def typeCode = new HashMap<String,String>()
			imaginPhy.annotatorConfidence= node.annotatorConfidence.'@value'
			imaginPhy.label=node.label.'@value'

			node.typeCode.each{typ->
				/* You can use toInteger() over the GPathResult object */
				map2.add(typ.attributes())
				// att1= typ.'@code'
				// att2= typ.code.name()
			}

			map.add(map2)
			imaginPhy.typeCode=map
			z.imagingPhysicalEntityCollection.add(imaginPhy)
			//println imaginPhy.toString()
		}
		//println map



	}
	void fillcalculationData(Object y,calculationResult z)
	{
		// iterate over CalculationData
		y.children().each {node ->
			def calc = new CalculationData()
			calc.value = node.value.'@value'
			//def map2=new ArrayList<HashMap<String,String>>()

			//calc.uniqueIdentifier=node.uniqueIdentifier.'@root'


			//calc.unitOfMeasure=node.unitOfMeasure.'@value'

			//node.dataType.each{typ->
			/* You can use toInteger() over the GPathResult object */
			//	 map2.add(typ.attributes())
			//	}
			//if(node.name()=='calculationDataCollection'){fillcalculationData(node,calc)}

			//calc.dataType = map2
			z.calculationDataCollection.add(calc)
			//println calc.value
		}

	}
	void fillCalculationResult(Object y,CalculationEntity z)
	{
		// iterate over CalculationResults
		//println 'zzzzzz'
		y.children().each {node ->
			def calc = new calculationResult()
			def map2=new ArrayList<HashMap<String,String>>()
			//println node.name()
			//calc.uniqueIdentifier=node.uniqueIdentifier.'@root'


			calc.unitOfMeasure=node.unitOfMeasure.'@value'
			//println calc.unitOfMeasure

			node.dataType.each{typ->
				/* You can use toInteger() over the GPathResult object */
				map2.add(typ.attributes())
			}
			fillcalculationData(node.calculationDataCollection,calc)

			calc.dataType = map2
			//println calc.dataType
			z.calculationResultCollection.add(calc)
			//println calc.dataType
		}
	}
	void fillcalculationEntity(Object y, Annotation z)
	{
		//<typeCode code="...." codeSystem="...." codeSystemName="...."/>
		//def map = new ArrayList<ArrayList<HashMap<String,String>>>()
		//println 'entro a '
		// iterate over CalculationEntitys
		y.children().each {node ->
			//println node.name()
			def calc = new CalculationEntity()
			def map2=new ArrayList<HashMap<String,String>>()

			calc.uniqueIdentifier=node.uniqueIdentifier.'@root'


			calc.description=node.description.'@value'

			node.typeCode.each{typ->
				/* You can use toInteger() over the GPathResult object */
				map2.add(typ.attributes())
			}
			fillCalculationResult(node.calculationResultCollection,calc)

			calc.typeCode = map2
			z.calculationEntityCollection.add(calc)
			//println calc.typeCode
		}
		//println map


	}

	void fillTDSCCollection(Object y,twoDimensionType z)
	{
		y.children().each {node ->
			
			String xx=node.x.'@value'
			String yy=node.y.'@value'
			String ind=node.coordinateIndex.'@value'
			
			def coord = new twoDSCCollection(coordinateIndex : Integer.parseInt(ind), x:Float.parseFloat(xx),y:Float.parseFloat(yy))
			
			z.twoDspatialCoordinateCollection.add(coord)
			
		}
	}
	void fillmarkupEntity(Object y,Annotation z)
	{
		// iterate over markupEntities

		y.children().each {node ->

			def markTYPE
			String imageref=node.imageReferenceUid.'@root'
			String referenframe = node.referencedFrameNumber.'@value'
			if(node.'@xsi:type'.text()=='TwoDimensionMultiPoint'){

				markTYPE =  new twoDimensionType(uniqueIdentifier:(String)node.uniqueIdentifier.'@root', 
					imageReferenceUid:imageref, 
					referencedFrameNumber:Integer.parseInt(referenframe),
					type:(String)node.'@xsi:type'.text())

				fillTDSCCollection(node.twoDimensionSpatialCoordinateCollection,markTYPE)
				//println markTYPE
			}
			z.markupEntityCollection.add(markTYPE)
		}


	}
	void fillAnnotations(Object y, AnnotationsAIM4 z)
	{

		//z.Uid = y.uniqueIdentifier.'@root'
		//println y.uniqueIdentifier.'@root'
		String uiddat=null


		//iterate by each annotation
		y.imageAnnotations.children().each {node ->
			def anno = new Annotation()


			anno.setUniqueIdentifier((String)node.uniqueIdentifier.'@root')
			String dt=node.dateTime."@value"
			//println dt
			//dt=repairDate(dt);
			def formats = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date dates=formats.parse(dt)
			println "formats .... : "+ dates.format("yyyy-MM-dd'T'HH:mm:ss");
			anno.dateTime=dates
			
			anno.setName((String)node.name."@value")
			anno.setComment((String)node.comment."@value")


			node.children().each {node2 ->


				//if (node2.name()=="imageAnnotationStatementCollection"){fillAnnotationsStatement(node2)}

				if (node2.name()=="imagingPhysicalEntityCollection"){fillimagingPhysicalEntity(node2,anno)}
				if(node2.name()=='calculationEntityCollection'){fillcalculationEntity(node2,anno)}
				if(node2.name()=='markupEntityCollection'){fillmarkupEntity(node2,anno)}
				//def tre = new DicomImageReferencedE()


			}

			z.getImageAnnotations().add(anno)
		}

	}


	// read number of patients and annotations saves into a class
	void fillAnnotationsClass()
	{

		list.each {
			if(it.name.endsWith('.xml'))
			{   num++
				String line
				br = new BufferedReader(new InputStreamReader(new FileInputStream(it.path)));
				while ((line = br.readLine()) !=null) {
					sb.append(line);
				}

				//println "sb.toString() = " + sb.toString()

				def ann = new AnnotationsAIM4()

				//map xml
				InputXML2= new XmlSlurper().parseText(sb.toString())
				temp=InputXML2.person.name.'@value'
				temp2=InputXML2.uniqueIdentifier.'@root'
				// add uid of annotationcollection
				ann.setUid((String)InputXML2.uniqueIdentifier.'@root')
				// add person from AIM.xml annotation
				fillPerson(InputXML2,ann);

				fillAnnotations(InputXML2,ann)

				ls.add(ann)



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


	}
	def repairDate={ dat->
		println dat
		String result
		def str=dat.split("T");
		println str
		for(String st : str){result=result+" "+st}
		println result
		return result.substring(4,result.length());
		
	}

	void printAnnotations()
	{
		println num
		setNumberPatients2.each{ k, v -> println "${k}:${v}" }
		println setNumberPatients2.size()


	}





}

