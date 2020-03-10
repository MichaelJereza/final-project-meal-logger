package com.example.android.meallogger.data;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.List;

import com.example.android.meallogger.utils.UsdaAPIUtils;


public class MealRepository implements APIQueryTask.Callback{

    private static final String TAG = MealRepository.class.getSimpleName();
    private MutableLiveData<Meal> mFinalMeal;
    private MutableLiveData<Status> mStatus;
    private MutableLiveData<List<FoodId>> mFoodChoice;

    public MealRepository(){
        mFinalMeal = new MutableLiveData<>();
        mFinalMeal.setValue(null);

        mStatus = new MutableLiveData<>();
        mStatus.setValue(Status.SUCCESS);

        mFoodChoice = new MutableLiveData<>();
        mFoodChoice.setValue(null);
    }

    public LiveData<Meal> returnMeal(){ return mFinalMeal; }
    public LiveData<Status> getStatus(){
        return mStatus;
    }
    public LiveData<List<FoodId>> getChoices(){ return mFoodChoice; }

    // 1/2 CallBack function for APIQueryTask
    // Gets ID, fetches food from ID
    public void handleSearchResults(ArrayList<FoodId> json){
        if(json.isEmpty()){
            Log.d(TAG, "!===Search Failed, No Results");
            mStatus.setValue(Status.ERROR);
            return;
        }
        mFoodChoice.setValue(json);
        Log.d(TAG, "!===Search Complete, Food Choices:"+mFoodChoice.getValue());
        mStatus.setValue(Status.DONE);
    }
    // 2/2 CallBack function for APIQueryTask
    // Get food details
    public void handleDetailResults(MealItem json){
        mFoodChoice.setValue(null);

        Log.d(TAG, "!===Food:"+json.description
                +"\nCalories:"+json.foodNutrients.get(2).amount+json.foodNutrients.get(2).nutrient.unitName
                +"\nServing Size:"+json.servingSize+json.servingSizeUnit);

        Meal newValue;
        if(mFinalMeal.getValue()!=null){
            newValue = mFinalMeal.getValue();

        } else{
            newValue = new Meal();
        }
        newValue.items.add(json);

        // Sum of meal content
        for(int i=0; i<json.foodNutrients.size(); i++){
            FoodNutrient ntr = json.foodNutrients.get(i);
            switch(ntr.nutrient.name){
                case "Energy":
                    newValue.totalNutrients.calories.amount+=ntr.amount;
                    break;
                case "Protein":
                    newValue.totalNutrients.protein.amount+=ntr.amount;
                    break;
                case "Total lipid (fat)":
                    newValue.totalNutrients.fat.amount+=ntr.amount;
                    break;
                case "Fatty acids, total saturated":
                    newValue.totalNutrients.saturatedFat.amount+=ntr.amount;
                    break;
                case "Fatty acids, total trans":
                    newValue.totalNutrients.transFat.amount+=ntr.amount;
                    break;
                case "Carbohydrates":
                    newValue.totalNutrients.carbohydrates.amount+=ntr.amount;
                    break;
                case "Sugars, total including NLEA":
                    newValue.totalNutrients.sugars.amount+=ntr.amount;
                    break;
                case "Iron, Fe":
                    newValue.totalNutrients.iron.amount+=ntr.amount;
                    break;
                case "Sodium, Na":
                    newValue.totalNutrients.sodium.amount+=ntr.amount;
                    break;
                case "Calcium, Ca":
                    newValue.totalNutrients.calcium.amount+=ntr.amount;
                    break;
                case "Cholesterol":
                    newValue.totalNutrients.cholesterol.amount+=ntr.amount;
                    break;
                default:
            }
        }

//        newValue.totalNutrients. += json..calories.value;
//        newValue.totalNutrients.protein.value += json.labelNutrients.protein.value;
//        newValue.totalNutrients.carbohydrates.value += json.labelNutrients.carbohydrates.value;
//        newValue.totalNutrients.fat.value += json.labelNutrients.fat.value;
//        newValue.totalNutrients.saturatedFat.value += json.labelNutrients.saturatedFat.value;
//        newValue.totalNutrients.transFat.value += json.labelNutrients.transFat.value;
//        newValue.totalNutrients.sugars.value += json.labelNutrients.sugars.value;
//        newValue.totalNutrients.calcium.value += json.labelNutrients.calcium.value;
//        newValue.totalNutrients.iron.value += json.labelNutrients.iron.value;
//        newValue.totalNutrients.sodium.value += json.labelNutrients.sodium.value;
//        newValue.totalNutrients.cholesterol.value += json.labelNutrients.cholesterol.value;

        mFinalMeal.setValue(newValue);

        mStatus.setValue(Status.SUCCESS);
    }

    public void addFoodItemtoMeal(String query){
        if(query!=null){
            String url = UsdaAPIUtils.buildFoodSearchURL(query);
            Log.d(TAG, "!===Searching: "+query+"\n"+url);
            mStatus.setValue(Status.LOADING);
            new APIQueryTask(this).execute(url, "search");
        } else{
            mStatus.setValue(Status.ERROR);
            Log.d(TAG, "!===Failed addFoodItem(): No Query");
        }
    }

    public void lookupFoodDetails(FoodId query){
        if(query!=null){
            String url = UsdaAPIUtils.buildFoodDetailsURL(query.fdcId);
            Log.d(TAG, "!===Querying:"+query.fdcId+"\n"+url);
            new APIQueryTask(this).execute(url, "detail");
        }
    }

    FoodNutrient MatchAndSumNutrients(FoodNutrient toSum, List<FoodNutrient> nList){
        int j = 0;
        while(!nList.get(j).nutrient.name.equals(toSum.nutrient.name)){
            j++;
        }
        toSum.amount += nList.get(j).amount;
        return toSum;
    }
}
