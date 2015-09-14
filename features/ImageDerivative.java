package features;

import ij.plugin.filter.Convolver;
import ij.process.ImageProcessor;

/**
 *
 * @author <a ref ="zianfanti@gmail.com"> Zian Fanti Gutierrez</a>
 * @version  0.0.2-0
 */
public class ImageDerivative {

    /** The <code>ImageProcessor</code> to calculate image derivatives */
    private ImageProcessor ip;

    /** The standar deviation for the Gaussian function */
    private double sigma;

    /** First partial derivative on X */
    private double[] dX;

    /** First partial derivative on Y */
    private double[] dY;

    /** Second partial derivative on X*/
    private double[] dXX;

    /** Second partial derivative on Y*/
    private double[] dYY;

    /** Second partial derivative on XY */
    private double[] dXY;

    /** Convolver object for convolutions */
    private Convolver convolve;
    /**
     * Empty contructor
     */
    public ImageDerivative() {
    }


    /**
     * This constructor sets the base image to obtain the derivatives, also sets the
     * value of the standard deviation of the Gaussian funtion.
     *
     * @param ip
     * @param sigma The standar deviation in the Gaussian function
     */
    public ImageDerivative(ImageProcessor ip, double sigma) {
        this.ip = ip;
        this.sigma = sigma;
        this.convolve = new Convolver();
    }


    /**
     *
     * @return
     */
    public ImageProcessor dX() {
        ImageProcessor image = ip.duplicate();
        GaussianDerivative gaussian = new GaussianDerivative();
        double[] gaussianFilter = gaussian.gaussianKernel(sigma);
        double[] gaussianFirstDerFilter = gaussian.firstDerivativeKernel(sigma);

        convolve.setNormalize(false);
        convolve.convolveFloat(image, gaussianFirstDerFilter, gaussianFirstDerFilter.length, 1);
        convolve.convolveFloat(image, gaussianFilter, 1, gaussianFilter.length);

        dX = (double[]) image.getPixels();
        return image;
    }


    /**
     *
     * @return a new <code>FloatProcessor</code> corresponding to the gradient in
     * vertical direction.
     */
    public ImageProcessor dY() {
        ImageProcessor image = ip.duplicate();
        GaussianDerivative gaussian = new GaussianDerivative();
        double[] gaussianFilter = gaussian.gaussianKernel(sigma);
        double[] gaussianFirstDerFilter = gaussian.firstDerivativeKernel(sigma);

        convolve.setNormalize(false);
        convolve.convolveFloat(image, gaussianFirstDerFilter, 1, gaussianFirstDerFilter.length);
        convolve.convolveFloat(image, gaussianFilter, gaussianFilter.length, 1);

        dY = (double[]) image.getPixels();
        return image;
    }


    /**
     *
     * @return a new <code>FloatProcessor</code> corresponding to the gradient in
     * vertical direction.
     */
    public ImageProcessor dXX() {
        ImageProcessor image = ip.duplicate();
        GaussianDerivative gaussian = new GaussianDerivative();
        double[] gaussianFilter = gaussian.gaussianKernel(sigma);
        double[] gaussianSecondDerFilter = gaussian.secondDerivativeKernel(sigma);

        convolve.setNormalize(false);
        convolve.convolveFloat(image, gaussianSecondDerFilter, gaussianSecondDerFilter.length, 1);
        convolve.convolveFloat(image, gaussianFilter, 1, gaussianFilter.length);

        dXX = (double[]) image.getPixels();
        return image;
    }


    /**
     *
     * @return a new <code>FloatProcessor</code> corresponding to the gradient in
     * vertical direction.
     */
    public ImageProcessor dYY() {
        ImageProcessor image = ip.duplicate();
        GaussianDerivative gaussian = new GaussianDerivative();
        double[] gaussianFilter = gaussian.gaussianKernel(sigma);
        double[] gaussianSecondDerFilter = gaussian.secondDerivativeKernel(sigma);

        convolve.setNormalize(false);
        convolve.convolveFloat(image, gaussianSecondDerFilter, 1, gaussianSecondDerFilter.length);
        convolve.convolveFloat(image, gaussianFilter, gaussianFilter.length, 1);

        dYY = (double[]) image.getPixels();
        return image;
    }


    /**
     *
     * @return a new <code>FloatProcessor</code> corresponding to the gradient in
     * vertical direction.
     */
    public ImageProcessor dXY() {
        ImageProcessor image = ip.duplicate();
        GaussianDerivative gaussian = new GaussianDerivative();
        double[] gaussianFirstDerFilter = gaussian.firstDerivativeKernel(sigma);

        convolve.setNormalize(false);
        convolve.convolveFloat(image, gaussianFirstDerFilter, 1, gaussianFirstDerFilter.length);
        convolve.convolveFloat(image, gaussianFirstDerFilter, gaussianFirstDerFilter.length, 1);

        dXY = (double[]) image.getPixels();
        return image;
    }


    /**
     *
     * @return
     */
    public static ImageProcessor dX(ImageProcessor image, double s) {
        GaussianDerivative gaussian = new GaussianDerivative();
        double[] gaussianFilter = gaussian.gaussianKernel(s);
        double[] gaussianFirstDerFilter = gaussian.firstDerivativeKernel(s);

        Convolver convolve = new Convolver();

        convolve.setNormalize(false);
        convolve.convolveFloat(image, gaussianFirstDerFilter, gaussianFirstDerFilter.length, 1);
        convolve.convolveFloat(image, gaussianFilter, 1, gaussianFilter.length);

        return image;
    }


    /**
     *
     * @return a new <code>FloatProcessor</code> corresponding to the gradient in
     * vertical direction.
     */
    public static ImageProcessor dY(ImageProcessor image, double s) {
        GaussianDerivative gaussian = new GaussianDerivative();
        double[] gaussianFilter = gaussian.gaussianKernel(s);
        double[] gaussianFirstDerFilter = gaussian.firstDerivativeKernel(s);

        Convolver convolve = new Convolver();

        convolve.setNormalize(false);

        convolve.convolveFloat(image, gaussianFirstDerFilter, 1, gaussianFirstDerFilter.length);
        convolve.convolveFloat(image, gaussianFilter, gaussianFilter.length, 1);

        return image;
    }


    /**
     *
     * @return a new <code>FloatProcessor</code> corresponding to the gradient in
     * vertical direction.
     */
    public static ImageProcessor dXX(ImageProcessor image, double s) {
        GaussianDerivative gaussian = new GaussianDerivative();
        double[] gaussianFilter = gaussian.gaussianKernel(s);
        double[] gaussianSecondDerFilter = gaussian.secondDerivativeKernel(s);

        Convolver convolve = new Convolver();

        convolve.setNormalize(false);

        convolve.convolveFloat(image, gaussianSecondDerFilter, gaussianSecondDerFilter.length, 1);
        convolve.convolveFloat(image, gaussianFilter, 1, gaussianFilter.length);

        return image;
    }


    /**
     *
     * @return a new <code>FloatProcessor</code> corresponding to the gradient in
     * vertical direction.
     */
    public static ImageProcessor dYY(ImageProcessor image, double s) {
        GaussianDerivative gaussian = new GaussianDerivative();
        double[] gaussianFilter = gaussian.gaussianKernel(s);
        double[] gaussianSecondDerFilter = gaussian.secondDerivativeKernel(s);

        Convolver convolve = new Convolver();

        convolve.setNormalize(false);

        convolve.convolveFloat(image, gaussianSecondDerFilter, 1, gaussianSecondDerFilter.length);
        convolve.convolveFloat(image, gaussianFilter, gaussianFilter.length, 1);

        return image;
    }


    /**
     *
     * @return a new <code>FloatProcessor</code> corresponding to the gradient in
     * vertical direction.
     */
    public static ImageProcessor dXY(ImageProcessor image, double s) {
        GaussianDerivative gaussian = new GaussianDerivative();
        double[] gaussianFirstDerFilter = gaussian.firstDerivativeKernel(s);

        Convolver convolve = new Convolver();

        convolve.setNormalize(false);

        convolve.convolveFloat(image, gaussianFirstDerFilter, 1, gaussianFirstDerFilter.length);
        convolve.convolveFloat(image, gaussianFirstDerFilter, gaussianFirstDerFilter.length, 1);

        return image;
    }


    private void flush() {
        this.dX = null;
        this.dY = null;
        this.dXX = null;
        this.dYY = null;
        this.dXY = null;
    }

    /**
     * @return the gradX
     */
    public double[] getDX() {
        if (dX == null) {
            dX();
        }
        return dX;
    }


    /**
     * @return the gradY
     */
    public double[] getDY() {
        if (dY == null) {
            dY();
        }
        return dY;
    }


    /**
     * @return the gradX
     */
    public double[] getDXX() {
        if (dXX == null) {
            dXX();
        }
        return dXX;
    }


    /**
     * @return the gradY
     */
    public double[] getDYY() {
        if (dYY == null) {
            dYY();
        }
        return dYY;
    }


    /**
     * @return the gradY
     */
    public double[] getDXY() {
        if (dXY == null) {
            dXY();
        }
        return dXY;
    }

    /**
     * @return the ip
     */
    public ImageProcessor getImageProcessor() {
        return ip;
    }


    /**
     * @param ip the ip to set
     */
    public void setImageProcessor(ImageProcessor ip) {
        this.ip = ip;
        flush();
    }


    /**
     * @return the sigma
     */
    public double getSigma() {
        return sigma;
    }


    /**
     * @param sigma the sigma to set
     */
    public void setSigma(double sigma) {
        this.sigma = sigma;
        flush();
    }
}
