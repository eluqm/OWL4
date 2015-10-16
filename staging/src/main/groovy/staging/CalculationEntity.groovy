package staging


class CalculationData
{
	double value
	
}
class calculationResult
{
	//def dimensionCollection = new ArrayList<Dimension>
	def dataType = new ArrayList<HashMap <String,String>>()
	String unitOfMeasure
	def calculationDataCollection = new ArrayList<CalculationData>()
	
}
class CalculationEntity
{	String uniqueIdentifier
	def typeCode = new ArrayList<HashMap <String,String>>()
	String description
	def calculationResultCollection = new ArrayList<calculationResult>()
}