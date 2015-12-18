package com.example.ruandongchuan.doubanmovie.ui.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruandongchuan on 15-11-10.
 */
public class InTheaterBean extends BaseBean {
    private String title;
    private int total;
    private int start;
    private int count;
    private ArrayList<SubjectBean> subjects;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<SubjectBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<SubjectBean> subjects) {
        this.subjects = subjects;
    }

    public static class SubjectBean extends BaseBean implements Serializable{
        private RatingBean rating;
        private ArrayList<String> genres;
        private int collect_count;
        private ArrayList<CastBean> casts;
        private String title;
        private String original_title;
        private String subtype;
        private ArrayList<DirectorBean> directors    ;
        private String year;
        private ImagesBean images;
        private String alt;
        private String id;

        public RatingBean getRating() {
            return rating;
        }

        public void setRating(RatingBean rating) {
            this.rating = rating;
        }

        public List<String> getGenres() {
            return genres;
        }

        public void setGenres(ArrayList<String> genres) {
            this.genres = genres;
        }

        public List<CastBean> getCasts() {
            return casts;
        }

        public void setCasts(ArrayList<CastBean> casts) {
            this.casts = casts;
        }

        public List<DirectorBean> getDirectors() {
            return directors;
        }

        public void setDirectors(ArrayList<DirectorBean> directors) {
            this.directors = directors;
        }

        public int getCollect_count() {
            return collect_count;
        }

        public void setCollect_count(int collect_count) {
            this.collect_count = collect_count;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public String getSubtype() {
            return subtype;
        }

        public void setSubtype(String subtype) {
            this.subtype = subtype;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public ImagesBean getImages() {
            return images;
        }

        public void setImages(ImagesBean images) {
            this.images = images;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public static class CastBean extends BaseBean implements Serializable{
            private String alt;
            private String id;
            private String name;
            private AvatarBean avatars;

            public String getAlt() {
                return alt;
            }

            public void setAlt(String alt) {
                this.alt = alt;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public AvatarBean getAvatars() {
                return avatars;
            }

            public void setAvatars(AvatarBean avatars) {
                this.avatars = avatars;
            }

            public static class AvatarBean extends ImagesBean implements Serializable{
                /*private String small;
                private String large;
                private String medium;

                public String getSmall() {
                    return small;
                }

                public void setSmall(String small) {
                    this.small = small;
                }

                public String getLarge() {
                    return large;
                }

                public void setLarge(String large) {
                    this.large = large;
                }

                public String getMedium() {
                    return medium;
                }

                public void setMedium(String medium) {
                    this.medium = medium;
                }*/
            }
        }
        public static class RatingBean implements Serializable{
            private int max;
            private float average;
            private int min;
            private String stars;

            public float getMax() {
                return max;
            }

            public void setMax(int max) {
                this.max = max;
            }

            public float getAverage() {
                return average;
            }

            public void setAverage(float average) {
                this.average = average;
            }

            public float getMin() {
                return min;
            }

            public void setMin(int min) {
                this.min = min;
            }

            public String getStars() {
                return stars;
            }

            public void setStars(String stars) {
                this.stars = stars;
            }
        }
        public static class DirectorBean implements Serializable{
            private String alt;
            private String id;
            private String name;
            private AvatarBean avatars;

            public String getAlt() {
                return alt;
            }

            public void setAlt(String alt) {
                this.alt = alt;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public AvatarBean getAvatars() {
                return avatars;
            }

            public void setAvatars(AvatarBean avatars) {
                this.avatars = avatars;
            }
            public static class AvatarBean implements  Serializable{
                private String small;
                private String large;
                private String medium;

                public String getSmall() {
                    return small;
                }

                public void setSmall(String small) {
                    this.small = small;
                }

                public String getLarge() {
                    return large;
                }

                public void setLarge(String large) {
                    this.large = large;
                }

                public String getMedium() {
                    return medium;
                }

                public void setMedium(String medium) {
                    this.medium = medium;
                }
            }
        }

        public static class ImagesBean implements Serializable{
            private String small;
            private String large;
            private String medium;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }
        }
    }
}
