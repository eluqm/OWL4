package staging

abstract class AnnotationEntity {
	
	
		// uniqueIdentifier from entity
		String uniqueIdentifier
		
		Date dateTime;
	
		String name;
	
		String typeCode;
	
		String comment;
		
		//templateUid
		//precedentPre....
	}
	//CD  = String
	
	
	// imageAnnotation
	class Annotation extends AnnotationEntity{
	
		//list of markupEntities
		def markupEntityCollection = new ArrayList<MarkupEntity>()
		//list of imagingPhysicalEntities
		def imagingPhysicalEntityCollection=new ArrayList<ImagingPhysicalEntity>()
		
		//list of calculationEntities
		def calculationEntityCollection= new ArrayList<CalculationEntity>()
	}