
public class MobliePhone
{
    String brand;
    String model;
    double price;

    public MobliePhone(String brand, String model, double price){
        this.brand=brand;
        this.model=model;
        if(price>=0) {
            this.price = price;
        }
        else{
            this.price=0;
        }
    }

    public void printDetails(){
        System.out.println("Brand: "+this.brand +" Model: "+this.model+" Price: "+this.price);
    }

    public void Discount() {
        this.price = this.price * 0.9;
    }

    public static void compering(MobliePhone Phone1,MobliePhone Phone2){
        if(Phone1.price>Phone2.price){
            System.out.println("the phone that cost more is: ");
            Phone1.printDetails();
        }
        else if(Phone2.price>Phone1.price){
            System.out.println("the phone that cost more is: ");
            Phone2.printDetails();
        }
        else{
            System.out.println("both phones cost the same ");
        }
    }
    public static void main(String[] args) {
        MobliePhone phone1 = new MobliePhone("Apple","Iphone 11",4500);
        MobliePhone phone2 = new MobliePhone("Samsung","Note 24",4300);

        phone1.printDetails();
        phone2.printDetails();

        phone1.Discount();
        phone2.Discount();

        phone1.printDetails();
        phone2.printDetails();

        compering(phone1,phone2);

    }
}
