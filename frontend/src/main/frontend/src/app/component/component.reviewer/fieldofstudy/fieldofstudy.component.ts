import {Component} from "@angular/core";
import {UserFieldOfStudyComponent} from "../../component.user/fieldofstudy/fieldofstudy.component";

@Component({
  selector: 'reviewerFieldsOfStudies',
  templateUrl: '../../component.user/fieldofstudy/fieldofstudy.component.html'
})
export class ReviewerFieldOfStudyComponent extends UserFieldOfStudyComponent {

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/reviewerFieldsOfStudies', id]);
  }
}
