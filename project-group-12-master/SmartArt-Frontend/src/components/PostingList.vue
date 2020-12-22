<template>
  <div style="padding-left: 100px; padding-right: 100px; padding-top: 50px">
    <div class="card-columns" style="margin-top: 50px; position: center">
      <div class="card-wrapper"
           v-bind:key="posting.id" v-for="posting in postingList">
          <div class="card"
               data-aos="fade-up"
               data-aos-duration="2000">
            <div class="card-img-wrapper">
              <img class="card-img-top" v-bind:src="posting.image"
                   onerror="this.onerror=null;this.src='https://i.ibb.co/RYk5c57/no-Image-Available.png';"
                   style="max-height: 220px; width: auto">
            </div>
            <div class="card-body">
              <h5 class="card-title">
                <p>{{posting.title}}</p>
                <p>${{posting.price}}</p>
              </h5>
              <div class="card-content">
                <p class="card-text">{{ posting.description }}</p>
                <button class="btn btn-danger" @click="toPosting(posting.postingID)">View Post</button>
              </div>
            </div>
          </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "PostingList",
  // props "exports" what's inside so that whatever imports PostingList has access to data inside
  props: ["postingList"],

  methods: {
    toPosting(id) {
      this.$store.dispatch("setActivePosting", id);
      this.$router.push({ name: "ViewPosting" });
    }
  }
}
</script>

<style scoped>
.btn {
  border-radius: 3px !important;
  border: none !important;
  box-shadow: none !important;
  transform: scale(1) !important;
}
  .card-wrapper {
    margin-bottom: 30px;
  }
  .card-image .card .card-img-wrapper {
    height: 100%;
  }
  .card-image .card .card-body {
    display: none;
    transition: 1.5s ease;
  }
  .card-image-title-description .card .card-img-wrapper {
    max-height: 160px;
  }
  .card-image-title-description .card {
    position: relative;
    min-height: 300px;
  }
  .card-image-title-description .card .card-body {
    height: auto;
    position: relative;
    top: 0;
    margin-bottom: -70px;
    max-width: 22rem;
  }
  .card-image-title-description .card:hover .card-body {
    top: -70px;
    min-width: 24rem;
  }
  .card-image-title-description .card .card-body .card-title {
    margin-bottom: .75rem;
    max-width: 22rem;
  }
  .card {
    display: inline-block;
    position: relative;
    overflow: hidden;
    min-height: 400px;
    max-height: 400px;
    height: 100%;
    max-width: 20rem;
    box-shadow: none;
    transition: all 0.5s cubic-bezier(.38,.41,.27,1)
  }
  .card:hover {
    box-shadow: 10px 15px 20px;
  }
  .card-img-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    overflow: hidden;
    max-width: 22rem;
  }
  .card-img-wrapper img {
    transition: 1s ease;
  }
  .card:hover .card-img-wrapper img {
    transform: scale(1.30);
  }
  .card-body .card-title {
    margin-bottom: calc(50% + 20px);
    transition: 1s ease;
  }
  .card:hover .card-body .card-title {
    margin-bottom: .75rem;
  }

  .card-body {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 50%;
    background-color: #F0F0F0;
    max-width: 22rem;
    transition: .5s ease;
  }
  .card-content {
    left: 0;
    right: 0;
    overflow: hidden;
    width: 100%;
    height: auto;
    transition: .5s ease;
  }
  .card:hover .card-body {
    height: 60%;
    background-color: #F8F8F8;
  }
  .card:hover .card-content {
    bottom: 0;
  }
</style>
