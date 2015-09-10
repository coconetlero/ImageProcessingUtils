package features;

import ij.process.ImageProcessor;

/**
 * Class that compute the Hessian matrix and extract it's corresponding
 * eigenvalues and eigenvectors from a 2D Image. This class order the value of
 * the eigenvalues from less to high been l1 < l2
 *
 * @author <a ref ="zianfanti@gmail.com"> Zian Fanti Gutierrez</a>
 * @version 0.0.1-1
 */
public class Hessian {

    private ImageProcessor ip;

    private float sigma;

    private float[] Ixx;

    private float[] Ixy;

    private float[] Iyy;

    private float l1[];

    private float l2[];

    public Hessian(ImageProcessor ip, float sigma) {
        this.ip = ip;
        this.sigma = sigma;
    }

    /**
     *
     * @param ip
     * @param sigma
     * @param process
     */
    public Hessian(ImageProcessor ip, float sigma, boolean process) {
        this(ip, sigma);
        if (process) {
            ImageDerivative derivative = new ImageDerivative(ip, sigma);
            this.Ixx = (float[]) derivative.dXX().getPixels();
            this.Ixy = (float[]) derivative.dXY().getPixels();
            this.Iyy = (float[]) derivative.dYY().getPixels();
        }
    }

    /**
     * Calculate Ixx, Iyy, Ixy elements of the Hessian Matrix
     */
    public void generateHessianMatrix() {
        ImageDerivative derivative = new ImageDerivative(ip, sigma);
        this.Ixx = (float[]) derivative.dXX().getPixels();
        this.Ixy = (float[]) derivative.dXY().getPixels();
        this.Iyy = (float[]) derivative.dYY().getPixels();
    }

    /**
     * Compute the smallest eigenvalue of the current 2 x 2 Hessian matrix
     *
     * @return the eigenvalue result of
     * <code>L2 = 1/ 2 * ((dxx + dyy) - SQRT((dxx - dyy)^2 + 4 dxy^2))</code>
     */
    public float[] smallEigenvalue() {
        double alpha = 0;
        double trace = 0;
        float[] smallEigenvalue = new float[ip.getWidth() * ip.getHeight()];

        for (int i = 0; i < smallEigenvalue.length; i++) {
            trace = Ixx[i] + Iyy[i];
            alpha = Math.sqrt(Math.pow(Ixx[i] - Iyy[i], 2) + (4 * (Ixy[i] * Ixy[i])));

            smallEigenvalue[i] = (float) ((trace - alpha) * 0.5);
        }
        return smallEigenvalue;
    }

    /**
     * Compute the largest eigenvalue of the current 2 x 2 Hessian matrix
     *
     * @return the eigenvalue result of
     * <code> L1 = 1/2 ((dxx + dyy) + SQRT((dxx - dyy)^2 + 4 dxy^2)) </code>
     */
    public float[] largeEigenvalue() {
        double alpha = 0;
        double trace = 0;

        float[] largeEigenvalue = new float[ip.getWidth() * ip.getHeight()];

        for (int i = 0; i < largeEigenvalue.length; i++) {
            trace = Ixx[i] + Iyy[i];
            alpha = Math.sqrt(Math.pow(Ixx[i] - Iyy[i], 2) + (4 * (Ixy[i] * Ixy[i])));

            largeEigenvalue[i] = (float) ((trace + alpha) * 0.5);
        }
        return largeEigenvalue;
    }

    /**
     * Compute the eigenvalues and asigns to the variables l1 and l2 depending
     * of this value.
     */
    public void computeEigenValues() {
        this.l1 = new float[ip.getWidth() * ip.getHeight()];
        this.l2 = new float[ip.getWidth() * ip.getHeight()];

        double alpha;
        double trace;

        for (int i = 0; i < l1.length; i++) {
            trace = Ixx[i] + Iyy[i];
            alpha = Math.sqrt(Math.pow(Ixx[i] - Iyy[i], 2) + (4 * (Ixy[i] * Ixy[i])));

            float root1 = (float) ((trace + alpha) * 0.5);
            float root2 = (float) ((trace - alpha) * 0.5);

            if (root1 < root2) {
                this.l1[i] = root1;
                this.l2[i] = root2;
            } else {
                this.l1[i] = root2;
                this.l2[i] = root1;
            }
        }
    }

    /**
     * Set sigma value for gaussian derivative
     *
     * @param sigma
     */
    public void setSigma(float sigma) {
        this.sigma = sigma;
    }

    /**
     * @return the l1
     */
    public float[] getL1() {
        return l1;
    }

    /**
     * @return the l2
     */
    public float[] getL2() {
        return l2;
    }

}
