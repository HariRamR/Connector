package com.hari.connector.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hari.connector.R
import com.hari.connector.databinding.ListItemContactBinding
import com.hari.connector.databinding.ListItemContactGridBinding
import com.hari.connector.interfaces.ContactInterface
import com.hari.connector.models.Data
import com.hari.connector.ui.Dashboard
import com.hari.connector.ui.UserProfileFrag

class ContactAdapter(private val contactInterface: ContactInterface) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var contactList: List<Data> = ArrayList()
    private val statusColors = listOf(R.color.green, R.color.orange, R.color.red)
    private var isLinearManager = true

    fun updateGridItems(contactList: List<Data>, isLinearManager: Boolean) {
        this.contactList = contactList
        this.isLinearManager = isLinearManager
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (isLinearManager) ListViewHolder(
            ListItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
        else GridViewHolder(
            ListItemContactGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ListViewHolder) {
            holder.userNameList.text = String.format(
                "%s %s",
                contactList[position].firstName,
                contactList[position].lastName
            )
            holder.userStatusMsgList.text = contactList[position].email

            val unwrappedDrawable =
                AppCompatResources.getDrawable(holder.itemView.context, R.drawable.status_bg)
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
            DrawableCompat.setTint(
                wrappedDrawable, ResourcesCompat.getColor(
                    holder.itemView.context.resources, statusColors[(0..2).random()],
                    holder.itemView.context.theme
                )
            )
            holder.userStatusIconList.setImageDrawable(wrappedDrawable)
            Glide.with(holder.itemView.context).load(contactList[position].avatar)
                .into(holder.userPhotoList)

        } else if (holder is GridViewHolder) {
            holder.userNameGrid.text = String.format(
                "%s %s",
                contactList[position].firstName,
                contactList[position].lastName
            )
            holder.userStatusMsgGrid.text = contactList[position].email

            val unwrappedDrawable =
                AppCompatResources.getDrawable(holder.itemView.context, R.drawable.status_bg)
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
            DrawableCompat.setTint(
                wrappedDrawable, ResourcesCompat.getColor(
                    holder.itemView.context.resources, statusColors[(0..2).random()],
                    holder.itemView.context.theme
                )
            )
            holder.userStatusIconGrid.setImageDrawable(wrappedDrawable)
            Glide.with(holder.itemView.context).load(contactList[position].avatar)
                .into(holder.userPhotoGrid)

        }

        holder.itemView.setOnClickListener {

            val profileFrag = UserProfileFrag()
            val bundle = Bundle().apply {
                putString(
                    "USERNAME",
                    String.format(
                        "%s %s",
                        contactList[holder.adapterPosition].firstName,
                        contactList[holder.adapterPosition].lastName
                    )
                )
                putString("EMAIL", contactList[holder.adapterPosition].email)
                putString("AVATAR", contactList[holder.adapterPosition].avatar)
            }

            profileFrag.arguments = bundle
            contactInterface.makeFABInVisible()

            (holder.itemView.context as Dashboard).supportFragmentManager.beginTransaction().apply {
                replace(R.id.profile_layout_dashboard, profileFrag)
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun getItemCount(): Int {

        return contactList.size
    }

    class ListViewHolder(itemView: ListItemContactBinding) : RecyclerView.ViewHolder(
        itemView.root
    ) {
        val userPhotoList = itemView.userPhotoListItemContact
        val userStatusIconList = itemView.statusIconListItemContact
        val userNameList = itemView.userNameListItemContact
        val userStatusMsgList = itemView.statusMsgListItemContact
    }

    class GridViewHolder(itemViewGrid: ListItemContactGridBinding) : RecyclerView.ViewHolder(
        itemViewGrid.root
    ) {
        val userPhotoGrid = itemViewGrid.userPhotoListItemContact
        val userStatusIconGrid = itemViewGrid.statusIconListItemContact
        val userNameGrid = itemViewGrid.userNameListItemContact
        val userStatusMsgGrid = itemViewGrid.statusMsgListItemContact
    }
}