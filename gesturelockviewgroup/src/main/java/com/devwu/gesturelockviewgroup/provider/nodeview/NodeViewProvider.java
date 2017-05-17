package com.devwu.gesturelockviewgroup.provider.nodeview;


import com.devwu.gesturelockviewgroup.nodeview.GestureLockNodeView;

/**
 * Created by WuNan on 17/5/16.
 * NodeView提供者，用于扩展
 */

public interface NodeViewProvider {
    GestureLockNodeView initChildView();
}
