package org.fleen.geom_2D;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * immutable 2d polygon
 * everything we can think of for making a useful polygon.
 */
public class DPolygon extends ArrayList<DPoint>{
  
  private static final long serialVersionUID=5206593635245516246L;

  /*
   * ################################
   * INIT
   * ################################
   */
  
  public DPolygon(){
    super();}
  
  public DPolygon(int s){
    super(s);}
  
  public DPolygon(DPoint... p){
    super(Arrays.asList(p));}
  
  public DPolygon(List<DPoint> p){
    super(p);}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */

  public Double signedarea=null;
  public Double perimeter=null;
  public DCircle incircle=null;
  
  public double[][] getPointArrays(){
    int s=size();
    double[][] a=new double[s][2];
    DPoint p;
    for(int i=0;i<s;i++){
      p=get(i);
      a[i]=new double[]{p.x,p.y};}
    return a;}
  
  public double getArea(){
    return Math.abs(getSignedArea());}
  
  public double getPerimeter(){
    if(perimeter==null)
      perimeter=GD.getPerimeter(getPointArrays());
    return perimeter;}
  
  public double getSignedArea(){
    if(signedarea==null)
      signedarea=GD.getSignedArea2D(getPointArrays());
    return signedarea;}
  
  public DCircle getIncircle(){
    if(incircle==null)
      incircle=IncircleCalculator.getIncircle(getPointArrays());
    return incircle;}
  
  /*
   * Center of the incircle
   * inside point furthest from edge
   * if there are multiple points then one is picked at random
   */
  public double[] getInPoint(){
    DCircle c=getIncircle();
    return new double[]{c.x,c.y};}
  
  /*
   * radius of incircle
   */
  public double getDepth(){
    DCircle c=getIncircle();
    return c.r;}
  
  /*
   * diameter of incircle
   */
  public double getDetailSize(){
    return getDepth()*2;}
  
  /*
   * index positive perimeter traversal direction
   * true means clockwise, false means counterclockwise
   */
  public boolean getTwist(){
    return getSignedArea()<0;}
  
  
  //assume twist is clockwise
  //TODO
  public boolean isConcave(){
//    int iprev,inext,s=points.length,angle;
//    boolean t=false;
//    for(int i=0;i<s;i++){
//      iprev=i-1;
//      if(iprev==-1)iprev=s-1;
//      inext=i+1;
//      if(inext==s)i=0;
//      angle=GKis.getRightAngle(get(iprev),get(i),get(inext));
//      if(angle>6)t=true;}
//    if(this.get
    return true;
  }
  
  public double getChunkiness(){
    return getArea()/getPerimeter();}
  
  /*
   * point containment test
   */
  public boolean containsPoint(double x,double y){
    Path2D p=getPath2D();
    return p.contains(x,y);}
  
  public boolean containsPoint(DPoint a){
    Path2D p=getPath2D();
    return p.contains(a.x,a.y);}
  
  /*
   * return the distaicne from the specified point to this polygon
   * test all seg distances, return the closest;
   */
  public double getDistance(double x,double y){
    double dtest,dclosest=Double.MAX_VALUE;
    for(DSeg seg:getSegs()){
      dtest=seg.getDistance(x,y);
      if(dtest<dclosest)
        dclosest=dtest;}
    return dclosest;}
  
  /*
   * return true if this polygon intersects the other polygon
   * intersection includes containment, either way. We're talking any kind of overlap at all.
   * that is to say, if they share any part of their area then we have intersection
   * 
   * if bounding rectangles intersect then maybe, otherwise false
   * if any vertex point of thispolygon is contained within otherpolygon then true
   * if any vertex point of otherpolygon is contained within thispolygon then true
   * if any seg in thispolygon intersects any seg in otherpolygon then true
   * otherwise false
   */
  public boolean intersect(DPolygon other){
    //test bounding rectangles for intersection
    Rectangle2D.Double 
      otherpolygonbounds=other.getBounds(),
      thispolygonbounds=getBounds();
    if(!GD.testIntersection_2Rectangles(
      thispolygonbounds.x,thispolygonbounds.y,thispolygonbounds.width,thispolygonbounds.height,
      otherpolygonbounds.x,otherpolygonbounds.y,otherpolygonbounds.width,otherpolygonbounds.height))
      return false;
    //test point containment
    for(DPoint p:this)
      if(other.containsPoint(p.x,p.y))
        return true;
    for(DPoint p:other)
      if(containsPoint(p.x,p.y))
        return true;
    //test seg intersection
     List<DSeg> 
       segs0=getSegs(),
       segs1=other.getSegs();
     for(DSeg s0:segs0){
       for(DSeg s1:segs1){
         if(s0.intersects(s1))
           return true;}}
     //all failed. no intersection
     return false;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * SEGS
   * ++++++++++++++++++++++++++++++++
   */
  
  private List<DSeg> segs=null;
  
  public List<DSeg> getSegs(){
    if(segs==null)
      initSegs();
    return segs;}
  
  private void initSegs(){
    segs=new ArrayList<DSeg>(size());
    int s=size(),i1;
    for(int i0=0;i0<s;i0++){
      i1=i0+1;
      if(i1==s)i1=0;
      segs.add(new DSeg(get(i0),get(i1)));}}
  
  /*
   * ################################
   * JAVA2D
   * ################################
   */
  
  /*
   * ++++++++++++++++++++++++++++++++
   * BOUNDING RECTANGLE
   * ++++++++++++++++++++++++++++++++
   */
  
  Rectangle2D.Double bounds=null;
  
  public Rectangle2D.Double getBounds(){
    if(bounds==null)
      initBounds();
    return bounds;}
  
  private void initBounds(){
    double[][] pa=getPointArrays();
    double maxx=Double.MIN_VALUE,maxy=maxx,minx=Double.MAX_VALUE,miny=minx;
    for(int i=0;i<pa.length;i++){
      if(minx>pa[i][0])minx=pa[i][0];
      if(miny>pa[i][1])miny=pa[i][1];
      if(maxx<pa[i][0])maxx=pa[i][0];
      if(maxy<pa[i][1])maxy=pa[i][1];}
    bounds=new Rectangle2D.Double(minx,miny,maxx-minx,maxy-miny);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * PATH2D
   * ++++++++++++++++++++++++++++++++
   */
  
  Path2D.Double path=null;
  
  public Path2D.Double getPath2D(){
    if(path==null)
      initPath2D();
    return path;}
  
  private void initPath2D(){
    path=new Path2D.Double();
//    path.setWindingRule(Path2D.WIND_NON_ZERO);
    DPoint p;
    p=get(0);
    path.moveTo(p.x,p.y);
    for(int i=1;i<size();i++){
      p=get(i);
      path.lineTo(p.x,p.y);}
    path.closePath();}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * MISC
   * we might get rid of these 
   * it's weird geometry for doing fluffing and we aren't doing fluffing
   * ++++++++++++++++++++++++++++++++
   */
  
  /*
   * returns true if any point in the specified polygon is in this polygon
   */
  public boolean sharesVertices(DPolygon polygon){
    for(DPoint p:polygon)
      if(contains(p))
        return true;
    return false;}
  
  /*
   * test for same size and equal points
   * test each point in the polygon
   * if an equal point exists within this polygon, 
   *   for all of those polygon points, then we have a sloppy match
   */
  public boolean sloppyMatch(DPolygon polygon){
    int s=polygon.size();
    if(s!=size())return false;
    //test each point in the param polygon
    //if we don't find its equal in this polygon then return false
    for(DPoint p:polygon)
      if(getEqualPoint(p)==null)
        return false;
    return true;}
  
  /*
   * returns the point in this polygon that is equal to the specified 
   */
  public DPoint getEqualPoint(DPoint p){
    for(DPoint p0:this)
      if(p0.equals(p))
        return p0;
    return null;}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public String toString(){
    StringBuffer a=new StringBuffer("[");
    for(DPoint p:this)
      a.append("("+p.x+","+p.y+"),");
    a.append("]");
    return a.toString();}
  
}
