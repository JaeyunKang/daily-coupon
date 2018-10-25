package com.daypon.app.daypon;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MembershipAdapter extends RecyclerView.Adapter<MembershipViewHolder> {

    private Context context;

    ArrayList<Membership> memberships = new ArrayList<>();

    public void add(Membership membership) {
        memberships.add(membership);
        notifyDataSetChanged();
    }

    @Override
    public MembershipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.membership_view, parent, false);
        return new MembershipViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MembershipViewHolder holder, int position) {
        Membership membership = memberships.get(position);
        holder.mNumText.setText(String.valueOf(membership.getNum()));
        holder.mMembershipRangeText.setText(membership.getStartDate() + " - " + membership.getEndDate());
        holder.mMembershipPayText.setText(membership.getStartDate());
        DecimalFormat format = new DecimalFormat("###,###");
        holder.mPriceText.setText(format.format(membership.getPrice()) + "원");
    }

    @Override
    public int getItemCount() {
        return memberships.size();
    }

}
