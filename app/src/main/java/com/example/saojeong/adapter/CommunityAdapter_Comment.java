package com.example.saojeong.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saojeong.R;
import com.example.saojeong.auth.TokenCase;
import com.example.saojeong.model.Community_Callback;
import com.example.saojeong.model.Community_CommentValue;
import com.example.saojeong.model.PostValue;
import com.example.saojeong.model.Post_CommentValue;
import com.example.saojeong.rest.ServiceGenerator;
import com.example.saojeong.rest.dto.board.CreateComentDto;
import com.example.saojeong.rest.dto.board.CreatePostDto;
import com.example.saojeong.rest.dto.board.GetPostDto;
import com.example.saojeong.rest.service.BoardService;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class CommunityAdapter_Comment extends RecyclerView.Adapter<CommunityAdapter_Comment.ViewHolder>{

    int board_id;
    int document_id;
    public RelativeLayout mLayout;
    public Context mContext;
    CommunityAdapter_Comment mAdapter;
    public boolean replies;
    private BoardService boardService;
    public static String LOG="Comment";
    Community_Callback refresh_callback;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private int comment_id;
        private TextView mTextView_ID;
        private TextView mTextView_Date;
        private TextView mTextView_Content;
        private TextView mTextView_Btn_ReComment;
        private TextView mTextView_RecommentID;
        private ImageView mImageView_Image;
        private LinearLayout mCommentLayout;
        private EditText mEditView_Recomment;
        private TextView mTextView_Btn_ReComment_Write;
        private RecyclerView mRecycleview;
        private NestedScrollView mNestedScroll;
//
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView_ID = (TextView) itemView.findViewById(R.id.tv_community_comment_id);
            mTextView_Date = (TextView) itemView.findViewById(R.id.tv_community_comment_date);
            mTextView_Content = (TextView) itemView.findViewById(R.id.tv_community_comment_contents);
            mTextView_Btn_ReComment = (TextView) itemView.findViewById(R.id.tv_community_comment_btn_recomment);
            mImageView_Image=(ImageView)itemView.findViewById(R.id.iv_community_comment_image);
            mEditView_Recomment=itemView.findViewById(R.id.et_community_comment_recomment);
            mTextView_Btn_ReComment_Write=itemView.findViewById(R.id.tv_community_btn_comment_write);
            mLayout=itemView.findViewById(R.id.item_community_layout);
            mCommentLayout=itemView.findViewById(R.id.ll_community_recomment);
            mTextView_RecommentID=itemView.findViewById(R.id.tv__recomment_name);
            mCommentLayout.setVisibility(View.GONE);
            SpannableString content = new SpannableString(mTextView_Btn_ReComment.getText());
            content.setSpan(new UnderlineSpan(), 0, mTextView_Btn_ReComment.getText().length(), 0);
            mTextView_Btn_ReComment.setText(content);
            mNestedScroll=itemView.findViewById(R.id.testscroll);

            mTextView_Btn_ReComment.setOnClickListener(v -> {
                if(mCommentLayout.getVisibility()==View.VISIBLE) {
                    mCommentLayout.setVisibility(View.GONE);
                }
                else {
                    mCommentLayout.setVisibility(View.VISIBLE);
                    mEditView_Recomment.requestFocus();
                    InputMethodManager  inputMananger = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMananger.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            });

            mTextView_Btn_ReComment_Write.setOnClickListener(v -> {
                String str=mEditView_Recomment.getText().toString(); //
                if(str.length()>0){
                    createComment(str, comment_id);
                    mEditView_Recomment.setText("");

                }

            });
            mRecycleview=itemView.findViewById(R.id.repliesRecycle);
            if(replies)
                mRecycleview.setVisibility(View.GONE);
        }
    }

    private List<Post_CommentValue> mContacts;

    public CommunityAdapter_Comment(List<Post_CommentValue> contacts, Context context, int board_id,int document_id, boolean replies, Community_Callback refresh_callback) {
        mContacts = contacts;
        this.board_id=board_id;
        this.replies=replies;
        mContext=context;
        this.document_id=document_id;
        boardService = ServiceGenerator.createService(BoardService.class, TokenCase.getToken());
        this.refresh_callback=refresh_callback;
    }

    @Override
    public CommunityAdapter_Comment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_community_comment, parent, false);

        CommunityAdapter_Comment.ViewHolder viewHolder = new CommunityAdapter_Comment.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommunityAdapter_Comment.ViewHolder holder, int position) {
        Post_CommentValue contact = mContacts.get(position);
        holder.mTextView_ID.setText(contact.getAuthor());
        holder.mTextView_Date.setText(contact.getCreatedAt());
        holder.mTextView_Content.setText(contact.getContent());
        holder.comment_id=contact.getComment_ID();
        holder.mTextView_RecommentID.setText(TokenCase.getUserResource("nickname"));
        CheckReComment(replies, holder);
        if(contact.getReplies()!=null) {
            mAdapter = new CommunityAdapter_Comment(contact.getReplies(), mContext, board_id, document_id, true, refresh_callback);
            holder.mRecycleview.setAdapter(mAdapter);
            holder.mRecycleview.setLayoutManager(new LinearLayoutManager(mContext));
            holder.mRecycleview.setNestedScrollingEnabled(false);
            holder.mRecycleview.smoothScrollToPosition(0);
        }
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }



    public void CheckReComment(boolean check, CommunityAdapter_Comment.ViewHolder holder) {
        if(check) {
            holder.mTextView_Btn_ReComment.setVisibility(View.GONE);
            holder.mImageView_Image.setVisibility(View.VISIBLE);
        }
        else {
            holder.mTextView_Btn_ReComment.setVisibility(View.VISIBLE);
            holder.mImageView_Image.setVisibility(View.GONE);
        }
    }

    public void createComment(String contents, int comment_id) {
        boardService.createRepliesComment(new CreateComentDto(contents), 10004, document_id, comment_id).enqueue(new Callback<CreateComentDto>() {
            @Override
            public void onResponse(Call<CreateComentDto> call, Response<CreateComentDto> response) {
                if (response.code() == 201) {
                    Log.d(LOG, "전송완료");
                    refresh_callback.callback();
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateComentDto> call, Throwable t) {
                Log.d("fail", t.getMessage());
            }
        });
        refresh_callback.callback();
    }





}

