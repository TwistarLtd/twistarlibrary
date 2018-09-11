# twistareventslibrary [![](https://jitpack.io/v/TwistarApp/twistareventslibrary.svg)](https://jitpack.io/#TwistarApp/twistareventslibrary)
This library is used to control the Twistar device events. After implementing this library a user can get callbacks of push and twist events.
![alt text](http://www.twistar.co/content/device-variant/maple-white-ceramic/oblique_maple.png)

# Motivation
This library is developed for the developers who want to work on Twistar Device and want to get its PUSH and TWIST events callback.

# Installation
To get a Git project into your build:

### Gradle

**Step 1.** Add the JitPack repository to your build file

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
  **Step 2.** Add the dependency
  
  ```
  dependencies {
	        implementation 'com.github.TwistarApp:twistareventslibrary:1.0.4'
	}
  ```
### Maven

**Step 1.** Add the JitPack repository to your build file

```
<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
  ```
  
  **Step 2.** Add the dependency
  
  ```
  <dependency>
	    <groupId>com.github.TwistarApp</groupId>
	    <artifactId>twistareventslibrary</artifactId>
	    <version>1.0.4</version>
	</dependency>
  ```

  # Implementation
  To implement this library in your own project. Implement **TwisterEventsCallback** interface to it.
  
  To initialise **Twistar** Events Library. Just add the below line to your Activity or Fragment and Pass context of class to it as an Argument. 
  
  ```
  new TwistarEventsApplication(this);
  ```
  
  To get the callback from **TwisterEventsCallback** interface we need to set the listener as
  
  ```
  twistarEventsApplication.setmTwisterEventsCallback(this);
  ```
  
  ### Example
  ```
  public class MainActivity extends AppCompatActivity implements TwisterEventsCallback {
   
    private TwistarEventsApplication twistarEventsApplication;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new TwistarEventsApplication(this);
        twistarEventsApplication.setmTwisterEventsCallback(this);
    }

    @Override
    public void onTwistLeftEventReceived() {
        Log.d(TAG, "onTwistLeftEventReceived: ");
    }

    @Override
    public void onTwistRightEventReceived() {
        Log.d(TAG, "onTwistRightEventReceived: ");
    }

    @Override
    public void onButtonPressEventReceived() {
        Log.d(TAG, "onButtonPressEventReceived: ");
    }

    @Override
    public void onButtonReleaseEventReceived() {
        Log.d(TAG, "onButtonReleaseEventReceived: ");
    }

    @Override
    public void onUnknownEventReceived() {
        Log.d(TAG, "onUnknownEventReceived: ");
    }
}
```
# Compatibility
- Minimum Android SDK: TwistarEventsLibrary 1.0.4 requires a minimum API level of 19.
- Compile Android SDK: TwistarEventsLibrary 1.0.4 requires you to compile against API 28 or later.

