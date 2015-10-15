package features;

import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import java.awt.geom.Point2D;

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

    private double l1[];

    private double l2[];

    private Point2D.Double[] ev1;

    private Point2D.Double[] ev2;

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
     * Compute the eigenvalues and assigns to the variables l1 and l2 depending
     * of this value. The eigenvalues are calculated from
     * <code> L1 = 1/2 ((dxx + dyy) +/- SQRT((dxx - dyy)^2 + 4 dxy^2)) </code>
     */
    public void computeEigenValues() {
        this.l1 = new double[ip.getWidth() * ip.getHeight()];
        this.l2 = new double[ip.getWidth() * ip.getHeight()];

        double alpha;
        double trace;

        for (int i = 0; i < l1.length; i++) {
            trace = Ixx[i] + Iyy[i];
            alpha = Math.sqrt(Math.pow(Ixx[i] - Iyy[i], 2) + (4 * (Ixy[i] * Ixy[i])));
            double root1 = (trace + alpha) * 0.5;
            double root2 = (trace - alpha) * 0.5;

            l1[i] = Math.min(root1, root2);
            l2[i] = Math.max(root1, root2);
        }
    }

    /**
     * Compute the absolute value of the eigenvalues and assigns to the
     * variables
     * l1 and l2 depending of this value. This is |l1| < |l2|
     */
    public void computeAbsEigenValues() {
        this.l1 = new double[ip.getWidth() * ip.getHeight()];
        this.l2 = new double[ip.getWidth() * ip.getHeight()];

        double alpha;
        double trace;

        for (int i = 0; i < l1.length; i++) {
            trace = Ixx[i] + Iyy[i];
            alpha = Math.sqrt(Math.pow(Ixx[i] - Iyy[i], 2) + (4 * (Ixy[i] * Ixy[i])));

            double root1 = (trace + alpha) * 0.5;
            double root2 = (trace - alpha) * 0.5;

            l1[i] = Math.abs(root1) <= Math.abs(root2) ? root1 : root2;
            l2[i] = Math.abs(root1) > Math.abs(root2) ? root1 : root2;
        }
    }

    /**
     * Compute the eigenvector corresponding to a large eigenvalue
     */
    public void largeEigenvector() {
        if (l1 != null) {
            this.ev1 = new Point2D.Double[ip.getWidth() * ip.getHeight()];

            double e1;
            double vx;
            double vy;
            double norm;

            for (int i = 0; i < ev1.length; i++) {
                e1 = l1[i];
                vx = -Ixy[i];
                vy = Ixx[i] - e1;

                norm = Math.sqrt(vx * vx + vy * vy);

                Point2D.Double ev = new Point2D.Double(vx / norm, vy / norm);
                ev1[i] = ev;
            }
        } else {
            this.computeEigenValues();
            this.largeEigenvector();
        }
    }

    /**
     * Compute the eigenvector corresponding to a small eigenvalue
     */
    public void smallEigenvector() {
        if (l2 != null) {
            this.ev2 = new Point2D.Double[ip.getWidth() * ip.getHeight()];

            double e2;
            double vx;
            double vy;
            double norm;

            for (int i = 0; i < ev2.length; i++) {
                e2 = l2[i];
                vx = -Ixy[i];
                vy = Ixx[i] - e2;

                norm = (float) Math.sqrt(vx * vx + vy * vy);

                Point2D.Double ev = new Point2D.Double(vx / norm, vy / norm);
                ev2[i] = ev;
            }
        } else {
            this.computeEigenValues();
            this.smallEigenvector();
        }
    }

    /**
     * Give the largest eigenvalue as a <code>FloatImageProcessor</code>
     *
     * @return
     */
    public ImageProcessor getL1AsImage() {
        return new FloatProcessor(ip.getWidth(), ip.getHeight(), l1);
    }

    /**
     * Give the largest eigenvalue as a <code>FloatImageProcessor</code>
     *
     * @return
     */
    public ImageProcessor getL2AsImage() {
        return new FloatProcessor(ip.getWidth(), ip.getHeight(), l2);
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
    public double[] getL1() {
        return l1;
    }

    /**
     * @return the l2
     */
    public double[] getL2() {
        return l2;
    }

    /**
     *
     * @return
     */
    public Point2D.Double[] getEV1() {
        return ev1;
    }

    /**
     *
     * @return
     */
    public Point2D.Double[] getEV2() {
        return ev2;
    }

}
