package staging


class algorithm{
	
	String name
	float version
}
class CalculationData
{
	float value
	
}
class calculationResult
{
	//def dimensionCollection = new ArrayList<Dimension>
	def dataType = new ArrayList<HashMap <String,String>>()
	String unitOfMeasure
	String type
	
	
}
class ExtendedCalculationResult extends  calculationResult
{
	def calculationDataCollection = new ArrayList<CalculationData>()
}
class CalculationEntity
{	String uniqueIdentifier
	def typeCode = new ArrayList<HashMap <String,String>>()
	String description
	def calculationResultCollection = new ArrayList<calculationResult>()
	def algoCollection = new ArrayList<algorithm>()
}