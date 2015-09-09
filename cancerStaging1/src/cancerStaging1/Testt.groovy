package cancerStaging1

import groovy.io.FileType

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.StringBuilder;

import org.omg.CORBA.TypeCode;

import groovy.transform.Field
import groovy.util.slurpersupport.NodeChildren;


class Testt {
	static void main(String[] args) {
		def a= new parserAIMFILES();
		println 'pasoo'
		a.readAnnotationsFiles();
		println 'paso2'
		a.fillAnnotationsClass();
		//println a.ls.size()
		//println a.ls[0].Uid
		//println a.ls[0].person.name
		//a.printAnnotations()
		
		
		
	}

}

class annotationCollection
{
	int idUI
	String namePatient
}
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
	def typeCode = new ArrayList<HashMap <String,String>>()
	def annotatorConfidence
	String label
}
interface AnnotationEntity {

	Date dateTime;

	String name;

	String typeCode;

	String comment;
}
//CD  = String

class Annotation implements AnnotationEntity{

	// uniqueIdentifier from AnnotationCollection abstrac class -> ImageAnnotationCollection
	String uniqueIdentifier
	
	//list of imagingPhysicalEntity
	def imagingPhysicalEntityCollection=new ArrayList<ImagingPhysicalEntity>()
	
	//list of calculationEntity
	def calculationEntityCollection=[]
}

class AnnotationsAIM4
{
	String Uid
	Person person
	
	//  imageAnnotationCollection= collection of Annotation
	def imageAnnotations=new ArrayList<Annotation>()

}



class parserAIMFILES
{
	
	def list=[]
	def pathLinux="/home/edson/Documentos/OWL4/OWL4/annotations"
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
		per.setName((String)(y.person.name.'@value'))
		per.setId((String)y.person.id.'@value')
		per.setSex((String)y.person.sex.'@value')
		// add 
		z.setPerson(per)
		
		
	}
	
	void fillAnnotationsStatement(Object y)
	{
		
		y.children().each {node ->
		println node.name()
		
		}
	}
	void fillimagingPhysicalEntity(Object y, Annotation z)
	{
		//z.imagingPhysicalEntityCollection
		def map = new ArrayList<ArrayList<HashMap<String,String>>>()
		
		
		y.children().each {node ->
		def imaginPhy = new ImagingPhysicalEntity()
		def map2=new ArrayList<HashMap<String,String>>()
			println 'entro a ...'+node.name()
			imaginPhy.uniqueIdentifier=node.uniqueIdentifier.'@root'
			def typeCode = new HashMap<String,String>()
			imaginPhy.annotatorConfidence= node.annotatorConfidence.'@value'
			imaginPhy.label=node.label.'@value'
			
			 node.typeCode.each{typ->
				/* You can use toInteger() over the GPathResult object */
				 map2.add(typ.attributes())
				// att1= typ.'@code'
				// att2= typ.code.name()
			   }
			
			/*if (node.typeCode.name() == 'typeCode')
			{
				println 'encontro ....' + node.typeCode.'@codeSystem'
				def temp = new HashMap<String,String>()
				node.each  { node2 ->
					//temp.put(node2.code.name(),node2.code.'@code') 
					
					println node2.'@codeSystem'
					println 'node node'
					//temp.put(node2.codeSystem.name(),node2.codeSystem.'@codeSystem')
					//temp.put(node2.codeSystemName.name(),node2.codeSystemName.'@codeSystemName')
					//temp.put(node2.odeSystemVersion.name(),node2.odeSystemVersion.'@odeSystemVersion')
				}
				//TypeCode.add(temp)
			}*/
			println map2
		z.imagingPhysicalEntityCollection.add(imaginPhy)
		map.add(map2)
		}
		println map
		
		
		
	}
	void fillAnnotations(Object y, AnnotationsAIM4 z)
	{
		//println y.person.name.'@value'
		//println y.uniqueIdentifier.'@root'
		z.Uid = y.uniqueIdentifier.'@root'
		
		//def anno = new Annotation()
		//z.imageAnnotations.add(anno)
		//iterate by each annotation
		y.imageAnnotations.children().each {node ->
			def anno = new Annotation()
			node.children().each {node2 ->
				//if (node2.name()=="imageAnnotationStatementCollection"){fillAnnotationsStatement(node2)}
				//println 'entro a fillhysicalEntityCollection'
				if (node2.name()=="imagingPhysicalEntityCollection"){fillimagingPhysicalEntity(node2,anno)}
				println anno.getImagingPhysicalEntityCollection().size()
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


	void printAnnotations()
	{
		println num
		setNumberPatients2.each{ k, v -> println "${k}:${v}" }
		println setNumberPatients2.size()


	}





}

	

