package staging

public class ImagingObservationEntity{

	/**
	 * annotatorConfidence is a real number, between 0 and 1, indicating annotator's
	 * confidence level.
	 */
	private float annotatorConfidence;
	/**
	 * Free text comment
	 */
	private String comment;
	/**
	 * A boolean value indicates whether or not an imaging observation exist in
	 * observed image(s).
	 */
	private Boolean isPresent;
	/**
	 * A human readable description. It can be used to store textual description of a
	 * question being asked. The question is related to questionTypeCode attribute.
	 */
	private String label;
	/**
	 * It is used to store an index value that identifies the order of each question
	 * in an imagingObservationEntity collection.
	 */
	Integer questionIndex;
	/**
	 * Coded entry data used to describe or capture the question being asked about
	 * imaging observation. It is associated with the typeCode.
	 */
	private String questionTypeCode;
	/**
	 * Coded entry data used to describe or capture an imaging observation.
	 */
	private String typeCode;
	private AnnotationEntity annotationEntity;
	//private ImagingObservationCharacteristic imagingObservationCharacteristicCollection;

	public ImagingObservationEntity(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * annotatorConfidence is a real number, between 0 and 1, indicating annotator's
	 * confidence level.
	 */
	/*public REAL GetAnnotatorConfidence(){
		return annotatorConfidence;
	}*/

	/**
	 * Free text comment
	 */
	/*public ST GetComment(){
		return comment;
	}*/

	/*public ImagingObservationCharacteristic[] GetImagingObservationCharacteristicCollection(){
		return null;
	}*/

	/**
	 * A boolean value indicates whether or not an imaging observation exist in
	 * observed image(s).
	 */
	/*public BL GetIsPresent(){
		return isPresent;
	}*/

	/**
	 * A human readable description. It can be used to store textual description of a
	 * question being asked. The question is related to questionTypeCode attribute.
	 */
	/*public ST GetLabel(){
		return label;
	}*/

	/*public INT GetQuestionIndex(){
		return questionIndex;
	}*/

	/**
	 * Coded entry data used to describe or capture the question being asked about
	 * imaging observation. It is associated with the typeCode.
	 */
	/*public CD GetQuestionTypeCode(){
		return questionTypeCode;
	}*/

	/**
	 * Coded entry data used to describe or capture an imaging observation.
	 */
	/*public CD GetTypeCode(){
		return typeCode;
	}*/

	/**
	 * annotatorConfidence is a real number, between 0 and 1, indicating annotator's
	 * confidence level.
	 * 
	 * @param newVal
	 */
	/*public void SetAnnotatorConfidence(REAL newVal){
		annotatorConfidence = newVal;
	}*/

	/**
	 * Free text comment
	 * 
	 * @param newVal
	 */
	/*public void SetComment(ST newVal){
		comment = newVal;
	}*/

	/**
	 * 
	 * @param imagingObservationCharacteristic
	 */
	/*public void SetImagingObservationCharacteristicCollection(ImagingObservationCharacteristic[] imagingObservationCharacteristic){

	}*/

	/**
	 * A boolean value indicates whether or not an imaging observation exist in
	 * observed image(s).
	 * 
	 * @param newVal
	 */
	/*public void SetIsPresent(BL newVal){
		isPresent = newVal;
	}*/

	/**
	 * A human readable description. It can be used to store textual description of a
	 * question being asked. The question is related to questionTypeCode attribute.
	 * 
	 * @param newVal
	 */
	/*public void SetLabel(ST newVal){
		label = newVal;
	}*/

	/**
	 * 
	 * @param newVal
	 */
/*public void SetQuestionIndex(INT newVal){
		questionIndex = newVal;
	}*/

	/**
	 * Coded entry data used to describe or capture the question being asked about
	 * imaging observation. It is associated with the typeCode.
	 * 
	 * @param newVal
	 */
	/*public void SetQuestionTypeCode(CD newVal){
		questionTypeCode = newVal;
	}*/

	/**
	 * Coded entry data used to describe or capture an imaging observation.
	 * 
	 * @param newVal
	 */
	/*public void SetTypeCode(CD newVal){
		typeCode = newVal;
	}*/
}//end ImagingObservationEntity
