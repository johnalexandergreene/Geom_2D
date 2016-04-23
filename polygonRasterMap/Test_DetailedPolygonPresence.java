package org.fleen.geom_2D.polygonRasterMap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;

/*
 * Draw a triangle
 * cast it to the raster map
 * render its presence in the cells of the raster map
 */
public class Test_DetailedPolygonPresence{
  
  /*
   * ################################
   * MAIN
   * ################################
   */
  
  public static final void main(String[] a){
    new Test_DetailedPolygonPresence();}
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Test_DetailedPolygonPresence(){
    initFrame();
    initRasterMap();
    polygoncellmap=rastermap.castPresence(polygon);
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
   * POLYGON
   * ################################
   */
  
  DPolygon polygon=new DPolygon(new DPoint(10,10),new DPoint(30,50),new DPoint(50,10));
//  DPolygon polygon=new DPolygon(new DPoint(10,10),new DPoint(12,13),new DPoint(14,10));
  
  /*
   * ################################
   * RASTER MAP
   * ################################
   */
  
  private static final int
    CELLARRAYWIDTH=100,
    CELLARRAYHEIGHT=100;
  private static final double GLOWSPAN=2.5;
  PolygonRasterMap rastermap;
  PolygonCells polygoncellmap;//the cells that feel the presence of the polygon
  
  void initRasterMap(){
    AffineTransform t=new AffineTransform();
    
//    t.translate(0,0);
//    t.rotate(0.1);
    
    rastermap=new PolygonRasterMap(CELLARRAYWIDTH,CELLARRAYHEIGHT,t,GLOWSPAN);}
  
  /*
   * ################################
   * RENDER
   * ################################
   */
  
  
  private static final double 
    RENDERSCALE=11;
  
  BufferedImage render(){
    BufferedImage image=new BufferedImage(800,800,BufferedImage.TYPE_INT_ARGB);
    Graphics2D g=image.createGraphics();
    
    //fill it
    g.setPaint(new Color(224,224,224));
    g.fillRect(0,0,800,800);
    
    //render all map cells
    Rectangle2D e=new Rectangle2D.Double(1,1,1,1);
    g.setPaint(Color.white);
    for(Cell[] row:rastermap.cells){
      for(Cell c:row){
        e.setFrame(c.x*RENDERSCALE-5,c.y*RENDERSCALE-5,10,10);
        g.fill(e);}}
    
    //++++++++++++++++++++++++++++++++++++++++
    //RENDER CELL POLYGON PRESENCES
    double presence;
    int alpha;
    
    //--------------------------------
    //primary edge cells
    //--------------------------------
    for(Cell c:polygoncellmap.primaryedgecells){
      presence=c.getPresenceIntensity(polygon);
      if(presence<0||presence>1)throw new IllegalArgumentException("primary edge cells -- PRESENCE OUT OF RANGE : "+presence);
      alpha=(int)(presence*255.0);
      g.setPaint(new Color(0,0,0,alpha));
      e.setFrame(c.x*RENDERSCALE-5,c.y*RENDERSCALE-5,10,10);
      g.fill(e);}
    
    //--------------------------------
    //edge exterior layers
    //--------------------------------
    Set<Cell> layer;
    for(int i=0;i<polygoncellmap.edgeexteriorlayers.size();i++){
      layer=polygoncellmap.edgeexteriorlayers.get(i);
      for(Cell c:layer){
        presence=c.getPresenceIntensity(polygon);
        if(presence<0||presence>1)throw new IllegalArgumentException("edge exterior layers -- PRESENCE OUT OF RANGE : "+presence);
        alpha=(int)(presence*255);
        if(i%2==0)
          g.setPaint(new Color(128,0,0,alpha));
        else
          g.setPaint(new Color(0,0,128,alpha));
        e.setFrame(c.x*RENDERSCALE-5,c.y*RENDERSCALE-5,10,10);
        g.fill(e);}}
    
    //--------------------------------
    //edge interior layers
    //--------------------------------
    for(int i=0;i<polygoncellmap.edgeinteriorlayers.size();i++){
      layer=polygoncellmap.edgeinteriorlayers.get(i);
      for(Cell c:layer){
        presence=c.getPresenceIntensity(polygon);
        if(presence<0||presence>1)throw new IllegalArgumentException("edge interior layers -- PRESENCE OUT OF RANGE : "+presence);
        alpha=(int)(presence*255.0);
        if(i%2==0)
          g.setPaint(new Color(128,0,0,alpha));
        else
          g.setPaint(new Color(0,0,128,alpha));
        e.setFrame(c.x*RENDERSCALE-5,c.y*RENDERSCALE-5,10,10);
        g.fill(e);}}
    
    //--------------------------------
    //interior layers
    //--------------------------------
    for(int i=0;i<polygoncellmap.interiorlayers.size();i++){
      layer=polygoncellmap.interiorlayers.get(i);
      for(Cell c:layer){
        presence=c.getPresenceIntensity(polygon);
        if(presence<0||presence>1)throw new IllegalArgumentException("interior layers -- PRESENCE OUT OF RANGE : "+presence);
        alpha=(int)(presence*255.0);
        if(i%2==0)
          g.setPaint(new Color(0,128,0,alpha));
        else
          g.setPaint(new Color(128,0,128,alpha));
        e.setFrame(c.x*RENDERSCALE-5,c.y*RENDERSCALE-5,10,10);
        g.fill(e);}}
    
    //-------------------------------
    //render polygon
    g.setPaint(new Color(255,255,0,128));
    g.setStroke(new BasicStroke(0.2f));
    g.setTransform(AffineTransform.getScaleInstance(RENDERSCALE,RENDERSCALE));
    Path2D path=polygon.getPath2D();
    g.draw(path);
    
    return image;}
  
}
