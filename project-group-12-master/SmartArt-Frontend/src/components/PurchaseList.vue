<template>
  <div style="padding-left: 100px; padding-right: 100px; padding-top: 50px">
    <div class="card-columns" style="margin-top: 50px; position: center">
      <div
        class="card-wrapper"
        v-bind:key="purchase.id"
        v-for="purchase in purchaseList"
      >
        <div
          class="card"
          style="width: 20rem"
          data-aos="fade-up"
          data-aos-duration="2000"
        >
          <div class="card-img-wrapper">
            <div class="card-img-wrapper">
              <img class="card-img-top" v-bind:src="purchase.postings[0].image" alt='../assets/noImageAvailable.png' style="max-height: 220px; width: auto">
            </div>
          </div>
          <div class="card-body">
            <h5 class="card-title">
              <p>Purchase ID: {{ purchase.purchaseID }}</p>
            </h5>
            <div class="card-content">
              <p class="card-text">Total Price: ${{ purchase.totalPrice }}</p>
              <p class="card-text">Purchased at: {{ purchase.time }}</p>
              <a
                href="#"
                class="btn btn-danger"
                @click="toPurchase(purchase.purchaseID)"
                >View Purchase</a
              >
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "PurchaseList",
  // props "exports" what's inside so that whatever imports PostingList has access to data inside
  props: ["purchaseList"],

  methods: {
    toPurchase(id) {
        this.$store.dispatch("setActivePurchase", id);
        this.$router.push({ name: "ViewPurchase" });
    },
  },
};
</script>

<style scoped>
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
  margin-bottom: 0.75rem;
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
  transition: all 0.5s cubic-bezier(0.38, 0.41, 0.27, 1);
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
  transform: scale(1.3);
}
.card-body .card-title {
  margin-bottom: calc(50% + 20px);
  transition: 1s ease;
}
.card:hover .card-body .card-title {
  margin-bottom: 0.75rem;
}

.card-body {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 50%;
  background-color: #F0F0F0;
  max-width: 22rem;
  transition: 0.5s ease;
}
.card-content {
  left: 0;
  right: 0;
  overflow: hidden;
  width: 100%;
  height: auto;
  transition: 0.5s ease;
}
.card:hover .card-body {
  height: 60%;
  background-color: #F8F8F8;
}
.card:hover .card-content {
  bottom: 0;
}
</style>
