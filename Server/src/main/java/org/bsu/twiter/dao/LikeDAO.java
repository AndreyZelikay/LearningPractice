package org.bsu.twiter.dao;

import org.bsu.twiter.models.Like;

public interface LikeDAO {
    boolean saveLike(Like like);
    boolean deleteLike(Like like);
    boolean isLikePresent(Like like);
}
