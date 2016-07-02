package cz.johrusk.myapp

import io.realm.RealmObject

/**
 * Created by Pepa on 02.07.2016.
 */

open class Recipe : RealmObject {

    constructor() {
    }

    constructor(t: String, b: String) {
        this.title = t
        this.body = b
    }

    var title: String? = null
    var body: String? = null
}
