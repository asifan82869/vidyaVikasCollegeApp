package shock.com.navigation.data

import com.google.firebase.database.Exclude

class Upload {
    private var mName: String? = null
    private var mImageUrl: String? = null
    private var mImageName: String? = null
    var isChecked:Boolean = false
    var mKey:String? = null


    constructor(){

    }

    constructor(name:String, imagName: String, imageUrl: String){
        if (name.trim() == ""){
            mName = "No name"
            mImageUrl = imageUrl
            mImageName = imagName
        }else{
            mName = name
            mImageUrl = imageUrl
            mImageName = imagName
        }
    }

    fun getName(): String?{
        return mName
    }

    fun getImageName(): String?{
        return mImageName
    }

    fun getImageUrl(): String? {
        return mImageUrl
    }

    @Exclude
    fun getKey(): String?{
        return mKey
    }

    fun setname(name:String){
        mName = name
    }

    fun setImageUrl(imageUrl:String){
        mImageUrl = imageUrl
    }
    fun setImageName(imageName:String){
        mImageName = imageName
    }

    @Exclude
    fun setKey(key:String){
        mKey = key
    }

}