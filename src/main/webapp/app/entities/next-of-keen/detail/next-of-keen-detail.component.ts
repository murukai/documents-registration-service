import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INextOfKeen } from '../next-of-keen.model';

@Component({
  selector: 'jhi-next-of-keen-detail',
  templateUrl: './next-of-keen-detail.component.html',
})
export class NextOfKeenDetailComponent implements OnInit {
  nextOfKeen: INextOfKeen | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nextOfKeen }) => {
      this.nextOfKeen = nextOfKeen;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
