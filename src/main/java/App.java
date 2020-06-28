
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    private ItemRepository itemRepository;
    private SalesPromotionRepository salesPromotionRepository;

    public App(ItemRepository itemRepository, SalesPromotionRepository salesPromotionRepository) {
        this.itemRepository = itemRepository;
        this.salesPromotionRepository = salesPromotionRepository;
    }

    public String bestCharge(List<String> inputs) {
        //TODO: write code here
        List<Item> items=itemRepository.findAll();
        HashMap<Item,Integer> bills=bill(inputs,items);

        StringBuffer order =detail(bills);





        return order.toString();
    }

    public HashMap<Item,Integer> bill(List<String>inputs,List<Item> items){
        HashMap<Item,Integer> bills =new HashMap<Item, Integer>();
        for (int i = 0; i <inputs.size() ; i++) {
            String[] s=inputs.get(i).split(" x ");
            for (int j= 0;j<items.size();j++){
                if (s[0].equals(items.get(j).getId())){
                    bills.put(items.get(j),Integer.parseInt(s[1]));
                }
            }
        }
        return bills;
    }



    public StringBuffer detail(HashMap<Item,Integer> bills){
        double total =0;
        double half_Price =0;
        int number =0;
        boolean half =false;
        List<String> cutFood=new ArrayList<>();
        List<SalesPromotion> salesPromotions=salesPromotionRepository.findAll();


        StringBuffer order =new StringBuffer();
        order.append("============= Order details =============\n");

        Iterator iterator=bills.keySet().iterator();

        while(iterator.hasNext()){
            Item item= (Item) iterator.next();
            number = bills.get(item);
            order.append(item.getName()+" x "+number+" = "+(int)(number*item.getPrice())+" yuan\n");
            total+=number*item.getPrice();

            if(salesPromotions.get(1).getRelatedItems().contains(item.getId())){
                half=true;
                half_Price+=(number*item.getPrice())/2;
                for (int i = 0; i <salesPromotions.get(1).getRelatedItems().size() ; i++) {
                    if(item.getId().equals(salesPromotions.get(1).getRelatedItems().get(i))){
                        cutFood.add(item.getName());
                    }
                }
            }
        }

        order.append("-----------------------------------\n");

        if(half){
            total-=half_Price;
            order.append("Promotion used:\n");
            order.append("Half price for certain dishes (");
            for (int i = 0; i < cutFood.size(); i++) {
                 if(i!=cutFood.size()-1){
                     order.append(cutFood.get(i)+"，");
                 }else{
                     order.append(cutFood.get(i)+")，saving "+(int)half_Price+" yuan\n");
                     order.append("-----------------------------------\n");
                 }
            }

        }

        if(total>=30){
            total -=6;
            order.append("Promotion used:\n" +
                    "满30减6 yuan，saving 6 yuan\n" +
                    "-----------------------------------\n");
        }

        order.append("Total："+(int)total+" yuan\n"+
                "===================================");

        return order;

    }




}
