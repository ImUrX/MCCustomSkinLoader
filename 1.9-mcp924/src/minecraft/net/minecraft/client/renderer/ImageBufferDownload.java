package net.minecraft.client.renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

public class ImageBufferDownload implements IImageBuffer
{
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;

    public BufferedImage parseUserSkin(BufferedImage image)
    {
        if (image == null)
        {
            return null;
        }
        else
        {
            int ratio = image.getWidth() / 64;
            this.imageWidth = 64 * ratio;
            this.imageHeight = 64 * ratio;
            BufferedImage bufferedimage = new BufferedImage(this.imageWidth, this.imageHeight, 2);
            Graphics graphics = bufferedimage.getGraphics();
            graphics.drawImage(image, 0, 0, null);
            if (image.getHeight() == 32 * ratio)
            {
                graphics.drawImage(bufferedimage, 24 * ratio, 48 * ratio, 20 * ratio, 52 * ratio,  4 * ratio, 16 * ratio,  8 * ratio, 20 * ratio, (ImageObserver)null);
                graphics.drawImage(bufferedimage, 28 * ratio, 48 * ratio, 24 * ratio, 52 * ratio,  8 * ratio, 16 * ratio, 12 * ratio, 20 * ratio, (ImageObserver)null);
                graphics.drawImage(bufferedimage, 20 * ratio, 52 * ratio, 16 * ratio, 64 * ratio,  8 * ratio, 20 * ratio, 12 * ratio, 32 * ratio, (ImageObserver)null);
                graphics.drawImage(bufferedimage, 24 * ratio, 52 * ratio, 20 * ratio, 64 * ratio,  4 * ratio, 20 * ratio,  8 * ratio, 32 * ratio, (ImageObserver)null);
                graphics.drawImage(bufferedimage, 28 * ratio, 52 * ratio, 24 * ratio, 64 * ratio,  0 * ratio, 20 * ratio,  4 * ratio, 32 * ratio, (ImageObserver)null);
                graphics.drawImage(bufferedimage, 32 * ratio, 52 * ratio, 28 * ratio, 64 * ratio, 12 * ratio, 20 * ratio, 16 * ratio, 32 * ratio, (ImageObserver)null);
                graphics.drawImage(bufferedimage, 40 * ratio, 48 * ratio, 36 * ratio, 52 * ratio, 44 * ratio, 16 * ratio, 48 * ratio, 20 * ratio, (ImageObserver)null);
                graphics.drawImage(bufferedimage, 44 * ratio, 48 * ratio, 40 * ratio, 52 * ratio, 48 * ratio, 16 * ratio, 52 * ratio, 20 * ratio, (ImageObserver)null);
                graphics.drawImage(bufferedimage, 36 * ratio, 52 * ratio, 32 * ratio, 64 * ratio, 48 * ratio, 20 * ratio, 52 * ratio, 32 * ratio, (ImageObserver)null);
                graphics.drawImage(bufferedimage, 40 * ratio, 52 * ratio, 36 * ratio, 64 * ratio, 44 * ratio, 20 * ratio, 48 * ratio, 32 * ratio, (ImageObserver)null);
                graphics.drawImage(bufferedimage, 44 * ratio, 52 * ratio, 40 * ratio, 64 * ratio, 40 * ratio, 20 * ratio, 44 * ratio, 32 * ratio, (ImageObserver)null);
                graphics.drawImage(bufferedimage, 48 * ratio, 52 * ratio, 44 * ratio, 64 * ratio, 52 * ratio, 20 * ratio, 56 * ratio, 32 * ratio, (ImageObserver)null);
            }
            graphics.dispose();
            this.imageData = ((DataBufferInt)bufferedimage.getRaster().getDataBuffer()).getData();
            if(!customskinloader.CustomSkinLoader.config.enableTransparentSkin){
                this.setAreaOpaque(0, 0, 32 * ratio, 16 * ratio);
                this.setAreaTransparent(32 * ratio, 0, 64 * ratio, 32 * ratio);
                this.setAreaOpaque(0, 16 * ratio, 64 * ratio, 32 * ratio);
                this.setAreaTransparent(0, 32 * ratio, 16 * ratio, 48 * ratio);
                this.setAreaTransparent(16 * ratio, 32 * ratio, 40 * ratio, 48 * ratio);
                this.setAreaTransparent(40 * ratio, 32 * ratio, 56 * ratio, 48 * ratio);
                this.setAreaTransparent(0, 48 * ratio, 16 * ratio, 64 * ratio);
                this.setAreaOpaque(16 * ratio, 48 * ratio, 48 * ratio, 64 * ratio);
                this.setAreaTransparent(48 * ratio, 48 * ratio, 64 * ratio, 64 * ratio);
            }
            return bufferedimage;
        }
    }

    public void skinAvailable()
    {
    }

    /**
     * Makes the given area of the image transparent if it was previously completely opaque (used to remove the outer
     * layer of a skin around the head if it was saved all opaque; this would be redundant so it's assumed that the skin
     * maker is just using an image editor without an alpha channel)
     */
    private void setAreaTransparent(int x, int y, int width, int height)
    {
        if (!this.hasTransparency(x, y, width, height))
        {
            for (int i = x; i < width; ++i)
            {
                for (int j = y; j < height; ++j)
                {
                    this.imageData[i + j * this.imageWidth] &= 16777215;
                }
            }
        }
    }

    /**
     * Makes the given area of the image opaque
     */
    private void setAreaOpaque(int x, int y, int width, int height)
    {
        for (int i = x; i < width; ++i)
        {
            for (int j = y; j < height; ++j)
            {
                this.imageData[i + j * this.imageWidth] |= -16777216;
            }
        }
    }

    /**
     * Returns true if the given area of the image contains transparent pixels
     */
    private boolean hasTransparency(int x, int y, int width, int height)
    {
        for (int i = x; i < width; ++i)
        {
            for (int j = y; j < height; ++j)
            {
                int k = this.imageData[i + j * this.imageWidth];

                if ((k >> 24 & 255) < 128)
                {
                    return true;
                }
            }
        }

        return false;
    }
}
