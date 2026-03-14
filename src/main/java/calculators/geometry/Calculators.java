package calculators.geometry;

public class Calculators {

    private Calculators() {
    }

    //计算矩形的面积、周长和对角线长
    public static double[] rectangle(double a, double b) {
        double[] results = new double[3];
        results[0] = a * b;               //面积
        results[1] = 2 * (a + b);         //周长
        results[2] = Math.sqrt(a * a + b * b); //对角线长
        return results;
    }

    //计算圆的半径、周长和面积
    public static double[] circle(byte choose, double num) {
        //根据输入的choose计算半径
        double r = 0;
        switch (choose) {
            case 1 -> r = num;                      //半径
            case 2 -> r = num / (2 * Math.PI);        //周长
            case 3 -> r = Math.sqrt(num / Math.PI); //面积
        }

        double[] results = new double[3];
        results[0] = r;                         //半径
        results[1] = (Math.PI * 2) * r;         //周长
        results[2] = Math.PI * r * r;           //面积
        return results;
    }

    //计算三角形面积
    public static double triangle(double a, double h) {
        return a * h / 2;
    }

    //计算平行四边形面积
    public static double rhomboid(double a, double h) {
        return a * h;
    }

    //计算梯形面积
    public static double trapezoid(double a, double b, double h) {
        return (a + b) * h / 2;
    }

    //计算圆锥体表面积和体积
    public static double[] cone(double r, double h) {
        double l = Math.sqrt(r * r + h * h);

        double[] results = new double[2];
        results[0] = Math.PI * r * r + Math.PI * r * l; //表面积
        results[1] = Math.PI * r * r * h / 3;          //体积
        return results;
    }

    //计算球体半径、表面积和体积
    public static double[] sphere(byte choose, double num) {
        //根据输入的choose计算半径
        double r = 0;
        switch (choose) {
            case 1 -> r = num;                          // 半径
            case 2 -> r = Math.sqrt(num / (4 * Math.PI)); // 表面积
            case 3 -> r = Math.pow(3 * num / (4 * Math.PI), 1.0/3); // 体积
        }

        double[] results = new double[3];
        results[0] = r;                         //半径
        results[1] = 4 * Math.PI * r * r;       //表面积
        results[2] = (4.0 / 3.0) * Math.PI * r * r * r; //体积
        return results;
    }

    //计算长方体对角线长、表面积和体积
    public static double[] cuboid(double a, double b, double h) {
        double[] result = new double[3];
        result[0] = Math.sqrt(a * a + b * b + h * h); //对角线长
        result[1] = 2 * (a * b + a * h + b * h);       //表面积
        result[2] = a * b * h;                     //体积
        return result;
    }

    //计算圆柱体表面积和体积
    public static double[] cylinder(double r, double h) {

        double[] results = new double[2];
        results[0] = 2 * Math.PI * r * r + 2 * Math.PI * r * h; // 表面积
        results[1] = Math.PI * r * r * h;                       // 体积
        return results;
    }
}

