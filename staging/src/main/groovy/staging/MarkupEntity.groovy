package staging

class MarkupEntity {

	
	// uniqueIdentifier from entity
	String type
	String uniqueIdentifier
	//void .....
	//void setuniqueIdentifier(String str)

}
class geometricShapeEntity extends MarkupEntity
{
String shapeIdentifier
boolean includeFlag
String lineColor
String lineOpacity
String lineStyle
String lineThickness



//@Override
//public void setuniqueIdentifier(String str) {
//	uniqueIdentifier=str
	// TODO Auto-generated method stub
	


}

class twoDSCCollection
{
int coordinateIndex
float x
float y
@Override
public String toString()
{
	return "ClassPojo [coordinateIndex = "+coordinateIndex+" x ="+ x +" y= "+ y+"]+"
}
}
//@Mixin([markupEntity,geometricShapeEntity])
class twoDimensionGeometricShapeEntity extends geometricShapeEntity
{
//type
String imageReferenceUid
int referencedFrameNumber
def twoDspatialCoordinateCollection = new ArrayList<twoDSCCollection>()
}

class twoDimensionType extends twoDimensionGeometricShapeEntity
{

@Override
public String toString()
{
	return "ClassPojo [type = "+type+", imagereferenceuid = "+imageReferenceUid+", referencedFramedNumber = "+referencedFrameNumber+", identifierEntity = "+uniqueIdentifier+"]";
}
}