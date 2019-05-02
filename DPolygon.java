package org.fleen.geom_2D;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * immutable 2d polygon
 * everything we can think of for making a useful polygon.
 * 
 * TODO put some of thse geometry methods into G2D
 * also organize G2D
 */
public class DPolygon extends ArrayList<DPoint>{
  
  private static final long serialVersionUID=5206593635245516246L;
  
  public Object object;//general purpose handy object

  /*
   * ################################
   * CONSTRUCTORS
   * 
   * Note
   *   If we are going to add points after construction, be careful about invoking
   *   any geometry analysis methods before we are done with that
   *   because cache
   *   
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
  
  public double getSignedArea(){
    if(signedarea==null)
      signedarea=GD.getSignedArea2D(getPointsAsDoubles());
    return signedarea;}
  
  public double getArea(){
    return Math.abs(getSignedArea());}
  
  public double getPerimeter(){
    if(perimeter==null)
      perimeter=GD.getPerimeter(getPointsAsDoubles());
    return perimeter;}
  
  /*
   * (aka twist)
   * The direction that we are going when tracing the points from index=0 to index=last. 
   * That is to say, the index positive perimeter traversal direction
   * true means clockwise, false means counterclockwise
   */
  public boolean getChirality(){
    return getSignedArea()<0;}
  
  /*
   * how thick and rounded this polygon. Circles are max thick.
   * the opposite of gangliness
   */
  public double getChunkiness(){
    return getArea()/getPerimeter();}
  
  /*
   * how floppyropey this polygon is
   * the opposite of chunkiness
   */
  public double getGangliness(){
    return getPerimeter()/getArea();}
 
  /*
   * ++++++++++++++++++++++++++++++++
   * POINTS AS DOUBLES
   * ++++++++++++++++++++++++++++++++
   */
    
  private double[][] pointsasdoubles=null;

  /*
   * we put the points of this polygon in this form for use in some of our
   * geometry analyzing methods 
   */
  public double[][] getPointsAsDoubles(){
    if(pointsasdoubles==null)
      initPointsAsDoubles();
    return pointsasdoubles;}
  
  public void initPointsAsDoubles(){
    int s=size();
    pointsasdoubles=new double[s][2];
    DPoint p;
    for(int i=0;i<s;i++){
      p=get(i);
      pointsasdoubles[i]=new double[]{p.x,p.y};}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * RELATIONSHIP BETWEEN POLYGON AND POINT
   * ++++++++++++++++++++++++++++++++
   */
  
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
   * return the distance from the specified point to this polygon's edge
   * test all seg distances, return the closest
   */
  public double getDistance(double x,double y){
    double dtest,dclosest=Double.MAX_VALUE;
    for(DSeg seg:getSegs()){
      dtest=seg.getDistance(x,y);
      if(dtest<dclosest)
        dclosest=dtest;}
    return dclosest;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * RELATIONSHIP BETWEEN 2 POLYGONS
   * ++++++++++++++++++++++++++++++++
   */
  
  /*
   * get distance between 2 closest segs on 2 polygons
   * TODO put in G2D?
   */
  public double getDistance(DPolygon p){
    double closest=Double.MAX_VALUE,test;
    for(DSeg s0:getSegs()){
      for(DSeg s1:p.getSegs()){
        test=s0.getDistance(s1);
        if(test<closest)
          closest=test;}}
    return closest;}
  
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
   * INCIRCLE
   * ++++++++++++++++++++++++++++++++
   */
  
  public DCircle incircle=null;
  
  public DCircle getIncircle(){
    if(incircle==null)
      incircle=IncircleCalculator.getIncircle(getPointsAsDoubles());
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
   * ++++++++++++++++++++++++++++++++
   * OUTCIRCLE
   * ++++++++++++++++++++++++++++++++
   */
  
  DCircle outcircle=null;
  
  /*
   * concentric to incircle
   * radius is distance from incircle center to furthest vertex
   */
  public DCircle getOutcircle(){
    if(outcircle==null)
      initOutcircle();
    return outcircle;}
  
  private void initOutcircle(){
    DCircle ic=getIncircle();
    double dtest,dfurthest=0;
    for(DPoint p:this){
      dtest=p.getDistance(ic.x,ic.y);
      if(dtest>dfurthest)
        dfurthest=dtest;}
    outcircle=new DCircle(ic.x,ic.y,dfurthest);}
  
  /*
   * test for the intersection of outcircles
   * test outcircles
   */
  public boolean intersectOutcircles(DPolygon other){
    DCircle 
      thisoc=getOutcircle(),
      otheroc=other.getOutcircle();
    double a=GD.getDistance_PointPoint(thisoc.x,thisoc.y,otheroc.x,otheroc.y);
    boolean i=a<(thisoc.r+otheroc.r);
    return i;}
  
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
   * ++++++++++++++++++++++++++++++++
   * INNER OUTER POLYGON
   * get the polygon by projecting inward or outward by a uniform distance.
   * handles trig at corners and such
   * we can get the polygon or we can get the vectors (dir,dis)
   * ++++++++++++++++++++++++++++++++
   */
  
  public List<DVector> getInnerOuterPolygonVectors(double offset){
    boolean clockwise=getChirality();
    int s=size(),iprior,inext;
    List<DVector> vectors=new ArrayList<DVector>(s);
    DPoint p,pprior,pnext;
    DVector v;
    for(int i=0;i<s;i++){
      iprior=i-1;
      if(iprior==-1)iprior=s-1;
      inext=i+1;
      if(inext==s)
      inext=0;
      p=get(i);
      pprior=get(iprior);
      pnext=get(inext);
      v=getInnerOuterPointVector(pprior,p,pnext,clockwise,offset);
      vectors.add(v);}
    return vectors;}
  
  private DVector getInnerOuterPointVector(DPoint p0,DPoint p1,DPoint p2,boolean clockwise,double offset){
    double angle,dir;
    if(clockwise){
      angle=GD.getAngle_3Points(p0.x,p0.y,p1.x,p1.y,p2.x,p2.y);
      dir=GD.getDirection_3Points(p0.x,p0.y,p1.x,p1.y,p2.x,p2.y);
    }else{  
      angle=GD.getAngle_3Points(p2.x,p2.y,p1.x,p1.y,p0.x,p0.y);
      dir=GD.getDirection_3Points(p2.x,p2.y,p1.x,p1.y,p0.x,p0.y);}
    if(angle>GD.PI)
      angle=GD.PI2-angle;
    double dis=offset/(GD.sin(angle/2));
    return new DVector(dir,dis);}
  
  public DPolygon getInnerOuterPolygon(double offset){
    List<DVector> vectors=getInnerOuterPolygonVectors(offset);
    int s=size();
    DPolygon pinnerouter=new DPolygon(s);
    DPoint p0,p1;
    DVector v;
    for(int i=0;i<s;i++){
      p0=get(i);
      v=vectors.get(i);
      p1=p0.getPoint(v);
      pinnerouter.add(p1);}
    return pinnerouter;}
  
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
    double[][] pa=getPointsAsDoubles();
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
  
  transient Path2D.Double path=null;
  
  public Path2D.Double getPath2D(){
    if(path==null)
      initPath2D();
    return path;}
  
  private void initPath2D(){
    path=new Path2D.Double();
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
  
  public Object clone(){
    DPolygon clone=new DPolygon();
    for(DPoint p:this){
      clone.add(new DPoint(p));}
    return clone;}
  
  public String toString(){
    StringBuffer a=new StringBuffer("[");
    for(DPoint p:this)
      a.append("("+p.x+","+p.y+"),");
    a.append("]");
    return a.toString();}
  
}
