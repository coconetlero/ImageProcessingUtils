package features;

/**
 * This class implement a scale space Gaussian [0 2]-th order derivative
 * convolution kernel for separated convolutions. Implements two version of gaussian
 * kernels.
 *
 * 1) Implements the Gaussian function:
 * <code>G(x,s) = (1/SQRT(2*PI)*s)*e(x^2/2*s^2)</code>
 * where s is the standar deviation of the gaussian function
 *
 * 2) Implements the Gaussian funtion defined in scale space theory
 * <code>G(x,t) = (1/SQRT(2*PI*t))e(x^2/2*t)</code>
 * where t is the scale of the features and <code>t=s^2</code>
 *
 * @author <a ref ="zianfanti@gmail.com"> Zian Fanti Gutierrez</a>
 * @version  0.3.0
 */
public class GaussianDerivative {

    /** PI value */
    private final double PI = Math.PI;

    public GaussianDerivative() {
    }

    /**
     * Compute the Gaussian function in 1D, for a value x and sigma t. The used
     * Gaussian function is: G(x) = (1 / sqrt(2·Pi·s^2)·e^(-x^2/2·s^2) where s
     * is the standar deviation for the gaussian function
     *
     * @param x
     * @param sigma  the standar deviation for the gaussian function
     * @return the value of the function for given parameters
     */
    private double gaussian(int x, float sigma) {
        double sigmaSquare = sigma * sigma;
        double norm = 1.0 / Math.sqrt(2.0 * PI * sigmaSquare);
        double gaussianFactor = Math.exp(-(x * x) / (2 * sigmaSquare));
        return gaussianFactor * norm;
    }

    /**
     * Compute the 1D discrete Gaussian kernel for a given sigma.
     *
     * @param sigma
     * @return
     */
    public float[] gaussianKernel(float sigma) {        
        int halfwidth = (int)(sigma * 4);
        int width = (halfwidth * 2) + 1;
        float[] kernel = new float[width];

        for (int x = -halfwidth; x <= halfwidth; x++) {
            kernel[x + halfwidth] = (float) gaussian(x, sigma);
        }
        return kernel;
    }

    /**
     * Compute the 1D discrete first derivative Gaussian kernel for a given sigma.
     *
     * @param sigma
     * @return
     */
    public float[] firstDerivativeKernel(float sigma) {
        int halfwidth = (int)(sigma * 4);
        int width = (halfwidth * 2) + 1;
        float[] kernel = new float[width];

        float sigmaSquare = sigma * sigma;
        for (int x = -halfwidth; x <= halfwidth; x++) {
            kernel[x + halfwidth] = -(x / sigmaSquare) * (float)gaussian(x, sigma);
        }
        return kernel;
    }

    /**
     * Compute the 1D discrete second derivative Gaussian kernel for a given sigma.
     * @param sigma
     * @return
     */
    public float[] secondDerivativeKernel(float sigma) {
        int halfwidth = (int)(sigma * 4);
        int width = (halfwidth * 2) + 1;
        float[] kernel = new float[width];
        
        float sigmaSquare = sigma * sigma;
        for (int x = -halfwidth; x <= halfwidth; x++) {
            kernel[x + halfwidth] = (float)((((x * x) - sigmaSquare) / Math.pow(sigma, 4)) * gaussian(x, sigma));
        }
        return kernel;
    }
}
