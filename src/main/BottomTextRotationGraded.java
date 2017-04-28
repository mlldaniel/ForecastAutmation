/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Daniel
 */
public class BottomTextRotationGraded extends BottomTextRotation{
    private int points;
    
    public BottomTextRotationGraded(String positionType, String packageType, String subpackage, String text){
        super(positionType, packageType, subpackage, text);
    }
    public BottomTextRotationGraded(String positionType, String packageType, String subpackage, String text, int points){
        super(positionType, packageType, subpackage, text);
        this.points = points;
    }
    public BottomTextRotationGraded(BottomTextRotation btr, int points){
        super(btr.getPositionType(), btr.getPackageType(), btr.getSubpackage(), btr.getText());
        this.points = points;
    }
    
    public void increasePointsBy(int i){
        setPoints(points+i);
    }
    /**
     * @return the points
     */
    public int getPoints() {
        return points;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(int points) {
        this.points = points;
    }
}
