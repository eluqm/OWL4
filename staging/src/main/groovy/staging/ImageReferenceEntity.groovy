package staging




/**
 * The Image class references the pixel data of images. It contains two UIDs. The
 * first UID can be used to identified a type of image e.g. CT, MR, CR, etc. It is
 * based on DICOM SOP Class UID. The second UID uniquely identifies an image. An
 * image is defined by its image plane, pixel data characteristics, gray scale
 * and/or color mapping characteristics, overlay planes and modality specific
 * characteristics (acquisition parameters and image creation information).
 *
 * A single image may contain a single frame of pixels or as multiple frames of
 * pixel data. The frames of a Multi-frame image (a cine run or the slices of a
 * volume) are sequentially ordered and share a number of common properties.
 * @author Pat
 * @version 1.0
 * @created 05-sep-2015 02:14:14 p.m.
 */
public class Image {

	/**
	 * Unique identifier specific for a Service-Object Pair (SOP) class, as specified
	 * in the DICOM standard.
	 */
	String sopClassUid;
	/**
	 * Unique identifier for a Service-Object Pair (SOP) instance, as specified in a
	 * DICOM tag.
	 */
	String sopInstanceUid;
	//private ImagePlane imagePlane;
	//private ImageSeries imageSeries;
	//private GeneralImage generalImage;

	/**
	 * Unique identifier specific for a Service-Object Pair (SOP) class, as specified
	 * in the DICOM standard.
	 */
	
}//end Image

/**
 * An abstract class that references the image being annotated.
 * @version 1.0
 * @created 05-sep-2015 02:09:57 p.m.
 */
abstract class ImageReferenceEntity {

	// from Entity
	String imageAnnotation;

	
}//end ImageReferenceEntity

class DicomImageReferencedE extends ImageReferenceEntity
{
	ImageStudy imagestudy
}
/**
 * Defines the characteristics of an imaging study performed on an entity. A study
 * in the AIM model has one series.
 * @author Pat
 * @version 1.0
 * @created 05-sep-2015 02:13:12 p.m.
 */
class ImageStudy {

	/**
	 * Unique identifier for an occurrence of a medical imaging study.
	 */
	String instanceUid;
	/**
	 * It is information about the procedure being performed on a subject.
	 */
	String procedureDescription;
	/**
	 * Date an imaging study started.
	 */
	String startDate;
	/**
	 * Time in HR(24):mm an imaging study started.
	 */
	String startTime;
	//private DicomImageReferenceEntity dicomImageReferenceEntity;
	ImageSeries imageSeries;
	//private ReferencedDicomObject referencedDicomObjectCollection;

	
}//end ImageStudy



/**
 * A series contains a set of images. Each series is associated with exactly one
 * Study.
 * @author Pat
 * @version 1.0
 * @created 05-sep-2015 02:13:47 p.m.
 */
class ImageSeries {

	/**
	 * Unique identifer of a study series.
	 */
	String instanceUid;
	/**
	 * Modality is the equipment used to acquire images of subjects or things, such as
	 * human and animal bodies. It is also used to reference other types of DICOM
	 * objects created by imaging modality or software program e.g. presentation state,
	 * SR document, radiotherapy objects, waveform, eModality is the equipment used to
	 * acquire images of subjects or things, such as human and animal bodies. It is
	 * also used to reference other types of DICOM objects created by imaging modality
	 * or software program e.g. presentation state, SR document, radiotherapy
	 * objectsncapsulated document, etc.
	 */
	String modality;
	//private ImageStudy imageStudy;
	def imageCollection= new ArrayList<Image>();

	
}//end ImageSeries
