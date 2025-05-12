import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

public class ReSizePICs {
    // פונקציה שמקבלת נתיב לתיקייה ומחזירה רשימת מצביעים לקבצים עם טיפול בשגיאות
    public static List<File> getFilesInDirectory(String directoryPath) {
        try {
            File directory = new File(directoryPath);
            // בדיקה אם הנתיב תקין
            if (!directory.exists()) {
                throw new IllegalArgumentException("שגיאה: הנתיב לא קיים.");
            }
            if (!directory.isDirectory()) {
                throw new IllegalArgumentException("שגיאה: הנתיב אינו ספרייה.");
            }
            File[] fileArray = directory.listFiles(); // קבלת רשימת הקבצים
            if (fileArray == null) {
                throw new RuntimeException("שגיאה: לא ניתן לקרוא את קבצי הספרייה.");
            }
            return Arrays.asList(fileArray); // מחזיר רשימת קבצים תקינה
        } catch (Exception e) {
            System.err.println("שגיאה במהלך קריאת הקבצים: " + e.getMessage());
            return List.of(); // מחזיר רשימה ריקה במקרה של שגיאה
        }
    }


    public static void resizeImages(List<File> imageFiles, String outputDirectory) {
        File outputDir = new File(outputDirectory);
        // בודקים אם תיקיית היעד קיימת ואם היא באמת תיקייה
        if (!outputDir.exists() || !outputDir.isDirectory()) {
            System.err.println("❌ שגיאה: תיקיית היעד לא קיימת או אינה תיקייה: " + outputDirectory);
            return;
        }
        // עוברים על כל קובץ ברשימת הקבצים
        for (File file : imageFiles) {
            try {
                // מנסים לקרוא את התמונה לקובץ BufferedImage (טוענים אותה לזיכרון)
                BufferedImage originalImage = ImageIO.read(file);
                // אם לא הצלחנו לקרוא את התמונה (null) – מדלגים עליה
                if (originalImage == null) {
                    System.out.println("⏩ דילוג על קובץ שאינו תמונה או פגום: " + file.getAbsolutePath());
                    continue;
                }
                // מחשבים את הגודל החדש – חצי מהרוחב והגובה המקוריים
                int newWidth = originalImage.getWidth() / 2;
                int newHeight = originalImage.getHeight() / 2;
                // יוצרים תמונה חדשה בגודל המוקטן ובפורמט RGB
                BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                // לוקחים עט ציור (Graphics2D) לצייר את התמונה המוקטנת
                Graphics2D g2d = resizedImage.createGraphics();
                // מציירים את התמונה המקורית מוקטנת על הקנבס החדש באיכות חלקה
                g2d.drawImage(originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
                // מסיימים לצייר – משחררים את המשאבים
                g2d.dispose();
                // קובעים את סוג הקובץ (jpg או png) לפי הסיומת המקורית
                String format = file.getName().toLowerCase().endsWith(".png") ? "png" : "jpg";
                // יוצרים קובץ חדש באותה תיקייה ובאותו שם לשמירת התמונה המוקטנת
                File outputFile = new File(outputDir, file.getName());
                // מדפיסים הודעה – מנסים לשמור את התמונה
                System.out.println("📝 מנסה לשמור תמונה ב: " + outputFile.getAbsolutePath());
                // מנסים לשמור את התמונה לקובץ
                boolean isSaved = ImageIO.write(resizedImage, format, outputFile);
                // מדפיסים הצלחה או כישלון בשמירה
                if (isSaved) {
                    System.out.println("✅ תמונה נשמרה בהצלחה ב: " + outputFile.getAbsolutePath());
                } else {
                    System.err.println("❌ כשל בשמירת התמונה: " + outputFile.getAbsolutePath());
                }
            } catch (IOException e) {
                // במקרה של שגיאה (כמו בעיית קריאה/שמירה) – מדפיסים הודעת שגיאה
                System.err.println("❌ שגיאה בעיבוד הקובץ: " + file.getAbsolutePath() + " - " + e.getMessage());
            }
        }
    }


    public static void convertImagesToGrayscale(List<File> imageFiles, String outputDirectory) {
        File outputDir = new File(outputDirectory);
        // בודקים אם תיקיית היעד קיימת ואם היא תיקייה אמיתית
        if (!outputDir.exists() || !outputDir.isDirectory()) {
            System.err.println("❌ שגיאה: תיקיית היעד לא קיימת או אינה תיקייה: " + outputDirectory);
            return;
        }
        for (File file : imageFiles) {
            try {
                BufferedImage originalImage = ImageIO.read(file);

                if (originalImage == null) {
                    System.out.println("⏩ דילוג על קובץ שאינו תמונה או פגום: " + file.getAbsolutePath());
                    continue;
                }
                // יוצרים תמונה חדשה באותו גודל – אבל בגווני אפור
                BufferedImage grayImage = new BufferedImage(
                        originalImage.getWidth(),
                        originalImage.getHeight(),
                        BufferedImage.TYPE_BYTE_GRAY
                );
                Graphics2D g2d = grayImage.createGraphics();
                g2d.drawImage(originalImage, 0, 0, null);
                g2d.dispose();
                // מוצאים את שם הקובץ והסיומת
                String fileName = file.getName();
                int dotIndex = fileName.lastIndexOf('.');
                String nameWithoutExtension = (dotIndex != -1) ? fileName.substring(0, dotIndex) : fileName;
                String extension = (dotIndex != -1) ? fileName.substring(dotIndex + 1).toLowerCase() : "jpg";
                // יוצרים שם חדש לקובץ עם "_gray" (אפור)
                String newFileName = nameWithoutExtension + "_gray." + extension;
                File outputFile = new File(outputDir, newFileName);
                System.out.println("🎨 מנסה לשמור תמונה בשחור לבן בשם: " + outputFile.getAbsolutePath());
                boolean isSaved = ImageIO.write(grayImage, extension.equals("png") ? "png" : "jpg", outputFile);
                if (isSaved) {
                    System.out.println("✅ נשמר: " + outputFile.getAbsolutePath());
                } else {
                    System.err.println("❌ כשל בשמירה: " + outputFile.getAbsolutePath());
                }
            } catch (IOException e) {
                System.err.println("❌ שגיאה בעיבוד הקובץ: " + file.getAbsolutePath() + " - " + e.getMessage());
            }
        }
    }

    // פונקציה ראשית
    public static void main(String[] args) {
        System.out.println("🚀 התוכנית התחילה לרוץ...");
        String path = "C:\\Users\\nehor\\IdeaProjects\\javaLearinig\\PICb4Resize"; // תיקיית מקור
        String pathNew = "C:\\Users\\nehor\\IdeaProjects\\javaLearinig\\PICafterResize"; // תיקיית יעד
        List<File> files = getFilesInDirectory(path);
        if (files.isEmpty()) {
            System.err.println("❌ אין תמונות לעיבוד!");
            return;}
        convertImagesToGrayscale(files, pathNew);
        //resizeImages(files, pathNew);
        System.out.println("✅ עיבוד התמונות הסתיים!");
    }
}