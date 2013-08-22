package segmentation;

import ij.process.ImageProcessor;
import ij.process.ImageStatistics;

/**
 * Class that implements a improved algorithm for otsu thresholding value used
 * in the image process to be binarized. All variables are been choosen acording
 * to the following paper.
 *
 * @author <a ref ="zianfanti@gmail.com"> Zian Fanti Gutierrez<a/>
 *
 * @Article{Jin Ling 2005,
 *    author =   "JIN Li-Sheng TIAN Lei WANG Rong-ben GUO Lie CHU Jiang-wei",
 *    title =    "An Improved Otsu Image Segmentation Algorithm for Path Mark
 *                Detection under Variable Illumination",
 *    journal =  "<i>Intelligent Vehicles Symposium, 2005. Proceedings. IEEE</i>",
 *    year =     "2005",
 *    pages =    "840--844",
 *    keywords = "otsu image binarization",
 *    note =     "improvment of otsu algorithm"
 *  }
 */
public class Otsu {

    /** Image to threshold */
    private ImageProcessor image;

    /** Delta for image */
    private double delta;

    /** An histogram of the image pixel values */
    private int[] histogram;

    /** The probability of the each value pixel on the image  */
    private double[] probability;

    /** the total number of pixels on the image */
    private int N;

    
    /**
     * Creates a new instance of Otsu
     * @param delta parameter for convergence. The proposed value are 0.1.
     */
    public Otsu(double delta) {
        this.delta = delta;
    }

    /**
     * Creates a new instance of Otsu 
     * 
     * @param image an <code>ImageProcessor</code> to convert in binary image.     
     * @param delta parameter for convergence. The proposed value are 0.1.
     */
    public Otsu(ImageProcessor image, double delta) {
        this.image = image;
        this.delta = delta;
        this.N = image.getWidth() * image.getHeight();
    }

    /**
     * Get the optimal threshold for image, the image must be one banded image.
     * @return a <code>double</code> the optimal value for thresholding image
     */
    public synchronized int threshold() {
        this.histogram = image.getHistogram();
        this.findProbability();

        ImageStatistics stats = image.getStatistics();
        int a0 = (int) stats.min;
        int a2 = (int) stats.max;
        int a1 = (a0 + a2) / 2;

        int th = check(a0, a1, a2);

        return th;
    }
    
    /**
     * Binarize this image using an Otsu method
     * 
     * @return a new <code>ImageProcessor</code> 
     */
    public ImageProcessor binarize() {
        int threshold = this.threshold();
        ImageProcessor binary = image.duplicate();
        binary.threshold(threshold);
        return binary;
    }

    /**
     * make the final checking for a1, if a1 is not correct then make one iteration more
     */
    private int check(int a0, int a1, int a2) {
        double f = this.f((int)a1);
        double f1 = this.f((int)a1 - 1) * this.f((int)a1 + 1);
       
        while ((Math.abs(f) > delta) && (f1 > 0)) {
            if (f >= 0) {
                a2 = a1;
            }
             else {
                a0 = a1;
            }
            
            a1 = (a0 + a2) / 2;
            f = this.f(a1);
            f1 = this.f(a1 - 1) * this.f(a1 + 1);
        }
        return a1;
    }

    private double f(int x) {
        if (x < 0) {
            return 0;
        } else if (x > histogram.length) {
            return histogram.length;
        } else {
            double m0 = this.m0(x);
            double m1 = this.m1(x);
            double f = (2 * x) - m0 - m1;

            return f;
        }
    }

    private double m0(int K) {
        double w0 = this.w0(K);
        double m0 = 0;

        for (int j = 0; j < K; j++) {
            m0 += (((double) j * probability[j]) / w0);
        }
        return m0;
    }

    private double m1(int K) {
        double w1 = this.w1(K);
        double m1 = 0;

        for (int j = K; j < probability.length; j++) {
            m1 += (((double) j * probability[j]) / w1);
        }
        return m1;
    }

    private double w0(int K) {
        double w0 = 0;
        for (int i = 0; i < K; i++) {
            w0 += probability[i];
        }
        return w0;
    }

    private double w1(int K) {
        double w1 = 0;
        for (int i = K; i < histogram.length; i++) {
            w1 += probability[i];
        }
        return w1;
    }

    /**
     * Return the probability of each pixel value, in the image.
     * In an array prob[bands][pixelSize]
     */
    private void findProbability() {
        this.probability = new double[histogram.length];
        for (int j = 0; j < histogram.length; j++) {
            probability[j] = histogram[j] / (double)N;
        }
    }

    /**
     * @param image the image to set
     */
    public void setImage(ImageProcessor image) {
        this.image = image;
        this.N = image.getWidth() * image.getHeight();
    }
}
