package com.vt.pamnpa.adapters;
import com.vt.pamnpa.room.Element;
import android.content.Context;
import android.view.LayoutInflater;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ElementListAdapter extends RecyclerView.Adapter<ElementListAdapter.ElementViewHolder> {
    List<Element> elements;
    LayoutInflater li;
    //…
    //w odróżnieniu od lab1c adapter nie otrzymuje listy elementów jako parametru konstruktora
    //w momencie tworzenia obiektu adaptera lista może nie być dostępna
    public ElementListAdapter(Context context) {
        li = LayoutInflater.from(context);
        elements = null;
    }
    @Override
    public int getItemCount() {
        //w momencie tworzenia obiektu adaptera lista może nie być dostępna
        if (elements != null)
            return elements.size();
        return 0;
    }
    //ponieważ dane wyświetlane na liście będą się zmieniały ta metoda umożliwia aktualizację
    //danych w adapterze (i w konsekwencji) wyświetlanych w RecyclerView
    public void setElementList(List<Element> elementList) {
        elementList = elementList;
        notifyDataSetChanged();
    }
    //…
}