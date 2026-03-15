package kk3twt.abnormal.tools.calculators.bmi;

/**
 * 提供 BMI 计算和体重分类的静态方法。
 *
 * @author CMCC-114514
 */
public class Calculators {

    /**
     * 计算身体质量指数（BMI）。
     * BMI = 体重(kg) / 身高(m)²
     *
     * @param weight 体重，单位：千克（kg），必须为正数
     * @param height 身高，单位：米（m），必须为正数
     * @return BMI 值
     * @throws IllegalArgumentException 如果体重或身高不大于0
     */
    public static double calculate(double weight, double height) {
        if (weight <= 0 || height <= 0) {
            throw new IllegalArgumentException("输入的体重或身高必须为正数！");
        }
        return weight / (height * height);
    }

    /**
     * 根据 BMI 值判断体重分类（采用中国参考标准）。
     *
     * @param bmi BMI 值
     * @return 体重分类描述，包含建议
     */
    public static String returnType(double bmi) {
        // BMI < 18.5：偏瘦
        if (bmi < 18.5)
            return "偏瘦，建议增加营养摄入";
            // 18.5 ≤ BMI < 24：正常范围
        else if (bmi >= 18.5 && bmi < 24)
            return "正常，请继续保持";
            // 24 ≤ BMI < 28：超重
        else if (bmi >= 24 && bmi < 28)
            return "偏重，建议科学规划饮食，抽空适当运动";
            // BMI ≥ 28：肥胖
        else
            return "肥胖，请控制饮食并适量运动";
    }
}