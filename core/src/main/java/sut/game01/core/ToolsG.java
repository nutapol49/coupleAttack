package sut.game01.core;

        import playn.core.*;
        import tripleplay.game.UIScreen;
        import tripleplay.util.Colors;
        import javax.tools.Tool;
        import static playn.core.PlayN.assets;
        import static playn.core.PlayN.graphics;


public class ToolsG extends UIScreen {
    private int width = 24;
    private int height = 18;
    public Integer time=0;

    public Layer genText(String text, int fontSize, Integer fontColor, Integer x, Integer y) {
        Font font = graphics().createFont("Alpha Taurus Expanded", Font.Style.PLAIN, fontSize);
        TextLayout layout = graphics().layoutText(
                text, new TextFormat().withFont(font).withWrapWidth(200));
        Layer textLayer = createTextLayer(layout, fontColor,x,y);
        return textLayer;
    }

    private Layer createTextLayer(TextLayout layout, Integer fontColor,Integer x,Integer y) {
        CanvasImage image = graphics().createImage(
                (int) (width / GameplayScreen.M_PER_PIXEL),
                (int) (height / GameplayScreen.M_PER_PIXEL));
        if (fontColor != null) image.canvas().setFillColor(fontColor);
        image.canvas().fillText(layout,x,y);

        return graphics().createImageLayer(image);
    }

    public float fade(float alphaTest) {
        if(alphaTest < 1f)
            return alphaTest + (float)0.1;
        else
            return 1f;
    }

    public float fade1(float alphaTest) {
        if(alphaTest > 0f)
            return alphaTest - (float)0.1;
        else
            return 0f;
    }



}