Normal Julia sets are a family of interesting mathematical objects.
Write an app to allow users to explore normal Julia sets and save their favourite snapshots in the cloud service bucket of your choice (GCS, AWS S3 .. ).
We're looking for an app that pays attention to helping users to explore these objects in an intuitive way, and runs smoothly (without too much waiting for screens to draw).
You should pay some attention to security (the use of public S3 buckets is deprecated :-) ).
We're not looking for bells and whistles - particularly given the amount of time available - but a reasonably understandable app that shows some sophistication in the way you render and explore the set.
If you have time, it might be fun to try and assist the user in searching for "interesting" parts of the parameter space.

You should not use third-party code to draw the set itself or for other core code.
You may use an external library to upload whatever data structures you upload to the bucket, but we will expect you to explain how it works.
You may (obviously) use libraries to provide utility components (controls, etc.)

We are not expecting you to do the hard maths. However, we do expect you to be able to look up how to draw a Julia set.