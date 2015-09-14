package features;

import ij.IJ;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import java.util.Arrays;

/**
 * Class that implements The Frangi multiscale vessel enhancement method using
 * ImageJ.
 *
 * @author <a ref ="zianfanti@gmail.com"> Zian Fanti Gutierrez</a>
 * @Article{Frangi 1997, author = "F. Frangi & w. Niessen", title = "Multiscale
 * vessel enhancement filtering", journal = "<i>Medical Image Computing and
 * Computer-Assisted Interventation</i>", year = "1997", volume = "1496", pages
 * = "130-137", keywords = ""}
 */
public class Frangi {

    private ImageProcessor ip;

    private static double Alpha;

    private static double Beta;

    private static double C;

    private static float MinSigma;

    private static float MaxSigma;
    
    private static int NumberOfSigmaSteps;
    
    // ---- monitor parameters ----
    private float currentSigma;

    /**
     *
     * @param alpha
     * @param beta
     * @param c
     * @param ip
     */
    public Frangi(double alpha, double beta, double c, ImageProcessor ip) {
        this.ip = ip;
        Frangi.Alpha = alpha;
        Frangi.Beta = beta;
        Frangi.C = c;
    }

    /**
     * 
     * @param min
     * @param max
     * @param steps
     * @return 
     */
    public ImageProcessor tubness2D(float min, float max, int steps) {
        Frangi.MaxSigma = max;
        Frangi.MinSigma = min;
        Frangi.NumberOfSigmaSteps = steps;
        
        
        double[] tubeness = new double[ip.getWidth() * ip.getHeight()];
        Arrays.fill(tubeness, Double.MIN_VALUE);
        
        float sigma = MinSigma;
        Hessian hessian = new Hessian(this.ip, sigma);        
        while (sigma <= MaxSigma) {            
            hessian.generateHessianMatrix();
            hessian.computeEigenValues();
            double[] l1 = hessian.getL1();
            double[] l2 = hessian.getL2();

            float scaleSquare = sigma * sigma;
            
            for (int i = 0; i < l2.length; i++) {
                // normalize over scale
                l1[i] *= scaleSquare;
                l2[i] *= scaleSquare;

                double Rb = l1[i] / l2[i];
                double S = (float) Math.sqrt((l1[i] * l1[i]) + (l2[i] * l2[i]));
                double V = (l2[i] > 0) ? Math.exp(-((Rb * Rb) / (2 * Beta * Beta)))
                        * (1 - Math.exp(-((S * S) / (2 * C * C)))) : 0.0;

                if (V > tubeness[i]) {
                    tubeness[i] = V;
                }
            }

            
            // Update sigma value in an equispaced way
            if (NumberOfSigmaSteps != 1) {
                float stepSize = (MaxSigma - MinSigma) / (NumberOfSigmaSteps - 1);
                sigma += stepSize;                          
            }
            else {
                sigma += MaxSigma;
            }            
            hessian.setSigma(sigma);            
            this.currentSigma = sigma;
        }
        
        return new FloatProcessor(this.ip.getWidth(), this.ip.getHeight(), tubeness);
    }

    /**
     * @return the minScale
     */
    public static double getMinScale() {
        return MinSigma;
    }

    /**
     * @param aMinScale the minScale to set
     */
    public static void setMinScale(float aMinScale) {
        MinSigma = aMinScale;
    }

    /**
     * @return the maxScale
     */
    public static double getMaxScale() {
        return MaxSigma;
    }

    /**
     * @param aMaxScale the maxScale to set
     */
    public static void setMaxScale(float aMaxScale) {
        MaxSigma = aMaxScale;
    }

    /**
     * @return the currentSigma
     */
    public float getCurrentSigma() {
        return currentSigma;
    }
}
