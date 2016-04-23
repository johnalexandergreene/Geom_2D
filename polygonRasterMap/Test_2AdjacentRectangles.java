package org.fleen.geom_2D.polygonRasterMap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;

/*
 * create 2 adjacent rectangles
 * colorize to distinguish from each other and background
 * render at different scales
 */
public class Test_2AdjacentRectangles{
  
  /*
   * ################################
   * MAIN
   * ################################
   */
  
  public static final void main(String[] a){
    new Test_2AdjacentRectangles();}
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Test_2AdjacentRectangles(){
    initFrame();
    initRasterMap();
    rastermap.castPresence(rectangle0,rectangle1);
    frame.repaint();}
  
  /*
   * ################################
   * FRAME
   * ################################
   */
  
  Frame0 frame;
  
  void initFrame(){
    frame=new Frame0();}
  
  @SuppressWarnings("serial")
  class Frame0 extends JFrame{
    
    Frame0(){
      this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      this.setBounds(100,100,800,800);
      this.setVisible(true);}
    
    public void paint(Graphics g){
      try{
        Graphics2D g2=(Graphics2D)g;
        g2.drawImage(render(),null,null);
      }catch(Exception x){}}}
  
  /*
   * ################################
   * POLYGONS
   * ################################
   */
  
  DPolygon 
    rectangle0=new DPolygon(new DPoint(0,0),new DPoint(0,10),new DPoint(10,10),new DPoint(10,0)),
    rectangle1=new DPolygon(new DPoint(10,0),new DPoint(10,10),new DPoint(20,10),new DPoint(20,0));
  
  /*
   * ################################
   * RASTER MAP
   * ################################
   */
  
  private static final int
    CELLARRAYWIDTH=800,
    CELLARRAYHEIGHT=800;
  private static final double GLOWSPAN=1.5;
  PolygonRasterMap rastermap;
  PolygonCells polygoncellmap;//the cells that feel the presence of the polygon
  
  void initRasterMap(){
    AffineTransform t=new AffineTransform();
    
    t.translate(60,60);
    t.rotate(0.2);
    t.scale(12.0,12.0);
    
    rastermap=new PolygonRasterMap(CELLARRAYWIDTH,CELLARRAYHEIGHT,t,GLOWSPAN);}
  
  /*
   * ################################
   * RENDER
   * ################################
   */
  
  int imagewidth=800,imageheight=800;
  
  BufferedImage render(){
    BufferedImage image=new BufferedImage(imagewidth,imageheight,BufferedImage.TYPE_INT_RGB);
    //render cells
    for(Cell c:rastermap)
      image.setRGB(c.x,c.y,getColor(c).getRGB());
    return image;}
  
/*
 * ################################
 * COLOR LOGIC
 * Given a cell, get the color for that cell
 * for each presence get the color for the associated polygon
 * do a weighted sum of r g b color components
 * 
 * TODO add an edge darkness. just a touch of darkness at the border. 
 * not really a line but enough to distinguish between 2 identically colored polygons
 * we could square the intensity, that gives us a curve that shrinks more at the bottom
 * or something
 * 
 * ################################
 */
  
  private static final Color 
    COLOR_R0=new Color(112,229,254),
    COLOR_R1=new Color(255,194,111);
  
  private Color getColor(Cell c){
    //get intensity sum
    double intensitysum=0;
    for(Presence p:c.presences)
      intensitysum+=p.intensity;
    //get normalized intensity for each presence and sum weighted r g b color components
    int r=0,g=0,b=0;
    Color color;
    double normalized;
    for(Presence p:c.presences){
      normalized=p.intensity/intensitysum;
      color=getPolygonColor(p.polygon);
      r+=(int)(color.getRed()*normalized);
      g+=(int)(color.getGreen()*normalized);
      b+=(int)(color.getBlue()*normalized);}
    //
    return new Color(r,g,b);}
  
  private Color getPolygonColor(DPolygon polygon){
    if(polygon==rectangle0)
      return COLOR_R0;
    else
      return COLOR_R1;}

}
