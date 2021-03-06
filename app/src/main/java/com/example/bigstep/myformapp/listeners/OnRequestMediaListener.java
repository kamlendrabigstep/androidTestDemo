package com.example.bigstep.myformapp.listeners;
/*
 *   Copyright (c) 2016 BigStep Technologies Private Limited.
 *
 *   You may not use this file except in compliance with the
 *   SocialEngineAddOns License Agreement.
 *   You may obtain a copy of the License at:
 *   https://www.socialengineaddons.com/android-app-license
 *   The full copyright and license information is also mentioned
 *   in the LICENSE file that was distributed with this
 *   source code.
 */


/**
 * A listener to handle media element events at view level.
 */
public interface OnRequestMediaListener {
    /**
     * Requesting for media when picker type element has been clicked.
     *
     * @param mediaType
     * @param fieldName
     */
    void onRequestMedia(String mediaType, String fieldName);
}
