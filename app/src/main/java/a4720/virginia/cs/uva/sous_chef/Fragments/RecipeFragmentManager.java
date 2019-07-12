package a4720.virginia.cs.uva.sous_chef.Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import a4720.virginia.cs.uva.sous_chef.Fragments.CookbookFragment;
import a4720.virginia.cs.uva.sous_chef.Fragments.FindRecipesFragment;
import a4720.virginia.cs.uva.sous_chef.R;

public class RecipeFragmentManager extends FragmentPagerAdapter {
    private Context mContext;

    public RecipeFragmentManager(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CookbookFragment();
        }
        else {
            return new FindRecipesFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.tab_a_label);
            case 1:
                return mContext.getString(R.string.tab_b_label);
            default:
                return null;
        }
    }
}
