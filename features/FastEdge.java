package features;

import ij.plugin.filter.Convolver;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

/**
 * Implements the Sobel and Laplacian filter in order to extract the edges from
 * the image. Also finds other edges operations.
 *
 * @author <a ref ="zianfanti@gmail.com"> Zian Fanti Gutierrez</a>
 */
public class FastEdge {

    /**
     * X direction Sobel kernel
     */
    private final float[] XKernel = {1, 2, 1, 0, 0, 0, -1, -2, -1};

    /**
     * Y direction Solbel Kernel
     */
    private final float[] YKernel = {1, 0, -1, 2, 0, -2, 1, 0, -1};

    /**
     * Laplacian 2nd order derivative Kernel
     */
    private final float[] LaplacianKernel = {1, 1, 1, 1, -8, 1, 1, 1, 1};

    /**
     * The <code>ImageProcessor</code> to work with it
     */
    private ImageProcessor image;
    
    /**
     *
     * @param image
     */
    public FastEdge(ImageProcessor image) {
        this.image = image.duplicate();                
    }
    
    /**
     * Compute the corresponding magnitude of the gradient on the base image,
     * computed using discrete derivatives of a Gaussian function.
     * Gradient is given by: |G| = (Gx^2 + Gy^2)^(1/2)
     * 
     * @return a new <code>ImageProcessor</code> formed by the gradient magnitud 
     * find on each pixel.
     */
    public ImageProcessor gradientMagnitude() {
        ImageProcessor dX_Image = this.image.duplicate();
        ImageProcessor dY_Image = this.image.duplicate();
        
        Convolver convolver = new Convolver();
        convolver.setNormalize(false);
        convolver.convolve(dX_Image, XKernel, 3, 3);
        convolver.convolve(dY_Image, YKernel, 3, 3);
        
        byte[] dX = (byte[]) dX_Image.getPixels();
        byte[] dY = (byte[]) dY_Image.getPixels();

        byte[] magnitude = new byte[dX.length];

        for (int i = 0; i < magnitude.length; i++) {
            float dx = (float) (0xff & dX[i]);
            float dy = (float) (0xff & dY[i]);
            magnitude[i] = (byte) Math.sqrt(dx * dx + dy * dy);
        }

        ImageProcessor gradient = new ByteProcessor(image.getWidth(), image.getHeight(), magnitude);
        return gradient;
    }
    
}
